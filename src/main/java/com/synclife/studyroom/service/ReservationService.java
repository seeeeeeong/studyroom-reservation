package com.synclife.studyroom.service;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import com.synclife.studyroom.domain.*;
import com.synclife.studyroom.dto.request.CreateReservationRequest;
import com.synclife.studyroom.dto.response.ReservationResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request, String userId) {
        roomRepository.findById(request.roomId())
            .orElseThrow(() -> new StudyroomException(ErrorCode.ROOM_NOT_FOUND));

        if (request.startAt().isAfter(request.endAt()) || request.startAt().equals(request.endAt())) {
            throw new StudyroomException(ErrorCode.RESERVATION_TIME_INVALID);
        }

        if (request.startAt().getMinute() % 30 != 0 || request.startAt().getSecond() != 0 ||
            request.endAt().getMinute() % 30 != 0 || request.endAt().getSecond() != 0) {
            throw new StudyroomException(ErrorCode.RESERVATION_TIME_NOT_ALIGNED);
        }

        long minutes = Duration.between(request.startAt(), request.endAt()).toMinutes();
        if (minutes < 30 || minutes % 30 != 0) {
            throw new StudyroomException(ErrorCode.RESERVATION_TIME_TOO_SHORT);
        }

        int startHour = request.startAt().getHour();
        int endHour = request.endAt().getHour();
        if (startHour < 9 || startHour >= 22 || endHour > 22 || (endHour == 22 && request.endAt().getMinute() > 0)) {
            throw new StudyroomException(ErrorCode.RESERVATION_OUTSIDE_OPERATING_HOURS);
        }

        try {
            Reservation reservation = Reservation.create(request.roomId(), userId, request.startAt(), request.endAt());
            Reservation savedReservation = reservationRepository.save(reservation);
            String roomName = roomRepository.findById(request.roomId()).map(Room::getName).orElse("Unknown Room");
            return ReservationResponse.from(savedReservation, roomName);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("reservations_no_overlap")) {
                throw new StudyroomException(ErrorCode.RESERVATION_TIME_CONFLICT);
            }
            throw e;
        }
    }

    @Transactional
    public void cancelReservation(Long reservationId, String userId, boolean isAdmin) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new StudyroomException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!isAdmin && !reservation.isOwnedBy(userId)) {
            throw new StudyroomException(ErrorCode.UNAUTHORIZED);
        }

        reservationRepository.delete(reservation);
    }
}
