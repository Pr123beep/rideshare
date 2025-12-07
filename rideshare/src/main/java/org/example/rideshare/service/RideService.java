package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public RideResponse createRide(CreateRideRequest request, String userId) {
        Ride ride = new Ride();
        ride.setUserId(userId);
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(new Date());

        Ride savedRide = rideRepository.save(ride);
        return mapToResponse(savedRide);
    }

    public List<RideResponse> getUserRides(String userId) {
        List<Ride> rides = rideRepository.findByUserId(userId);
        return rides.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RideResponse> getPendingRideRequests() {
        List<Ride> rides = rideRepository.findByStatus("REQUESTED");
        return rides.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RideResponse acceptRide(String rideId, String driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!ride.getStatus().equals("REQUESTED")) {
            throw new BadRequestException("Ride is not in REQUESTED status");
        }

        ride.setDriverId(driverId);
        ride.setStatus("ACCEPTED");

        Ride savedRide = rideRepository.save(ride);
        return mapToResponse(savedRide);
    }

    public RideResponse completeRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!ride.getStatus().equals("ACCEPTED")) {
            throw new BadRequestException("Ride must be ACCEPTED before completion");
        }

        ride.setStatus("COMPLETED");

        Ride savedRide = rideRepository.save(ride);
        return mapToResponse(savedRide);
    }

    private RideResponse mapToResponse(Ride ride) {
        RideResponse response = new RideResponse();
        response.setId(ride.getId());
        response.setUserId(ride.getUserId());
        response.setDriverId(ride.getDriverId());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropLocation(ride.getDropLocation());
        response.setStatus(ride.getStatus());
        response.setCreatedAt(ride.getCreatedAt());
        return response;
    }
}

