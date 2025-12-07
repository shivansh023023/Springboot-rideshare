package org.example.rideshare.controller;

import jakarta.validation.Valid;
import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // USER: create ride
    @PostMapping("/rides")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request,
                                                   Authentication authentication) {
        String username = authentication.getName();
        RideResponse response = rideService.createRide(username, request);
        return ResponseEntity.ok(response);
    }

    // USER: get own rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<RideResponse>> getMyRides(Authentication authentication) {
        String username = authentication.getName();
        List<RideResponse> rides = rideService.getUserRides(username);
        return ResponseEntity.ok(rides);
    }

    // USER/DRIVER: complete ride
    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String rideId,
                                                     Authentication authentication) {
        String username = authentication.getName();
        RideResponse res = rideService.completeRide(username, rideId);
        return ResponseEntity.ok(res);
    }
}
