package org.example.rideshare.controller;

import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverRideController {

    private final RideService rideService;

    public DriverRideController(RideService rideService) {
        this.rideService = rideService;
    }

    // DRIVER: view all pending requests
    @GetMapping("/rides/requests")
    public ResponseEntity<List<RideResponse>> getPendingRides() {
        List<RideResponse> rides = rideService.getPendingRideRequests();
        return ResponseEntity.ok(rides);
    }

    // DRIVER: accept ride
    @PostMapping("/rides/{rideId}/accept")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable String rideId,
                                                   Authentication authentication) {
        String username = authentication.getName();
        RideResponse res = rideService.acceptRide(username, rideId);
        return ResponseEntity.ok(res);
    }
}
