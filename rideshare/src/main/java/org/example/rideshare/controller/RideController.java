package org.example.rideshare.controller;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.model.User;
import org.example.rideshare.service.RideService;
import org.example.rideshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private UserService userService;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private User getCurrentUser() {
        String username = getCurrentUsername();
        return userService.findByUsername(username);
    }

    @PostMapping("/rides")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("ROLE_USER")) {
            throw new BadRequestException("Only users can request rides");
        }
        RideResponse response = rideService.createRide(request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/rides")
    public ResponseEntity<List<RideResponse>> getUserRides() {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("ROLE_USER")) {
            throw new BadRequestException("Only users can view their rides");
        }
        List<RideResponse> rides = rideService.getUserRides(currentUser.getId());
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<RideResponse>> getPendingRideRequests() {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("ROLE_DRIVER")) {
            throw new BadRequestException("Only drivers can view pending ride requests");
        }
        List<RideResponse> rides = rideService.getPendingRideRequests();
        return ResponseEntity.ok(rides);
    }

    @PostMapping("/driver/rides/{rideId}/accept")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable String rideId) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("ROLE_DRIVER")) {
            throw new BadRequestException("Only drivers can accept rides");
        }
        RideResponse response = rideService.acceptRide(rideId, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(response);
    }
}

