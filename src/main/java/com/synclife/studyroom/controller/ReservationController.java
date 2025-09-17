package com.synclife.studyroom.controller;

import com.synclife.studyroom.dto.request.CreateReservationRequest;
import com.synclife.studyroom.dto.response.ReservationResponse;
import com.synclife.studyroom.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ReservationResponse createReservation(@RequestBody CreateReservationRequest request,
        HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        return reservationService.createReservation(request, userId);
    }

    @DeleteMapping("/reservations/{id}")
    public void cancelReservation(@PathVariable Long id, HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        String userType = (String) httpRequest.getAttribute("userType");
        reservationService.cancelReservation(id, userId, userType);
    }
}
