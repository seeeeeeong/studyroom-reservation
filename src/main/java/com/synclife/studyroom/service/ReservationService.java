package com.synclife.studyroom.service;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import com.synclife.studyroom.domain.*;
import com.synclife.studyroom.dto.request.CreateReservationRequest;
import com.synclife.studyroom.dto.response.ReservationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CanceledReservationRepository canceledReservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository,
        CanceledReservationRepository canceledReservationRepository,
        RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.canceledReservationRepository = canceledReservationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request, String userId) {
        Room room = findRoomById(request.roomId());
        Reservation reservation = Reservation.create(room, userId, request.startAt(), request.endAt());
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId, String userId, String userType) {
        Reservation reservation = findReservationById(reservationId);
        validateCancelPermission(reservation, userId, userType);
        moveToCanceled(reservation, userId);
    }

    private Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
            .orElseThrow(() -> new StudyroomException(ErrorCode.ROOM_NOT_FOUND));
    }

    private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new StudyroomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    private void validateCancelPermission(Reservation reservation, String userId, String userType) {
        boolean isAdmin = "ADMIN".equals(userType);
        boolean isOwner = reservation.isOwnedBy(userId);

        if (isAdmin || isOwner) {
            return;
        }

        throw new StudyroomException(ErrorCode.RESERVATION_CANCEL_FORBIDDEN);
    }

    private void moveToCanceled(Reservation reservation, String canceledBy) {
        CanceledReservation canceledReservation = CanceledReservation.fromReservation(reservation, canceledBy);
        canceledReservationRepository.save(canceledReservation);
        reservationRepository.delete(reservation);
    }
}
