package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.RideRepository;
import org.example.rideshare.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public RideService(RideRepository rideRepository, UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    public RideResponse createRide(String username, CreateRideRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!"ROLE_USER".equals(user.getRole())) {
            throw new BadRequestException("Only ROLE_USER can create rides");
        }

        Ride ride = new Ride(user.getId(), request.getPickupLocation(), request.getDropLocation(), "REQUESTED");
        rideRepository.save(ride);
        return toResponse(ride);
    }

    public List<RideResponse> getUserRides(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return rideRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<RideResponse> getPendingRideRequests() {
        return rideRepository.findByStatus("REQUESTED")
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RideResponse acceptRide(String username, String rideId) {
        User driver = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only ROLE_DRIVER can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED status");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        rideRepository.save(ride);

        return toResponse(ride);
    }

    public RideResponse completeRide(String username, String rideId) {
        User actor = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in ACCEPTED status");
        }

        // Allow user or driver
        boolean isPassenger = actor.getId().equals(ride.getUserId());
        boolean isDriver = actor.getId().equals(ride.getDriverId());

        if (!isPassenger && !isDriver) {
            throw new BadRequestException("Only passenger or driver can complete the ride");
        }

        ride.setStatus("COMPLETED");
        rideRepository.save(ride);

        return toResponse(ride);
    }

    private RideResponse toResponse(Ride ride) {
        RideResponse res = new RideResponse();
        res.setId(ride.getId());
        res.setUserId(ride.getUserId());
        res.setDriverId(ride.getDriverId());
        res.setPickupLocation(ride.getPickupLocation());
        res.setDropLocation(ride.getDropLocation());
        res.setStatus(ride.getStatus());
        res.setCreatedAt(ride.getCreatedAt());
        return res;
    }
}
