package com.synclife.studyroom.controller;

import com.synclife.studyroom.config.CurrentUser;
import com.synclife.studyroom.dto.request.CreateReservationRequest;
import com.synclife.studyroom.dto.response.ReservationResponse;
import com.synclife.studyroom.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ReservationResponse createReservation(@Valid @RequestBody CreateReservationRequest request,
        @CurrentUser String userId) {
        return reservationService.createReservation(request, userId);
    }

    @DeleteMapping("/reservations/{id}")
    public void cancelReservation(@PathVariable Long id, @CurrentUser String userId) {
        reservationService.cancelReservation(id, userId, false);
    }

    @DeleteMapping("/admin/reservations/{id}")
    public void adminCancelReservation(@PathVariable Long id, @CurrentUser String adminId) {
        reservationService.cancelReservation(id, adminId, true);
    }
}
