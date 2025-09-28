package com.synclife.studyroom.service;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import com.synclife.studyroom.domain.Room;
import com.synclife.studyroom.domain.RoomRepository;
import com.synclife.studyroom.domain.Reservation;
import com.synclife.studyroom.domain.ReservationRepository;
import com.synclife.studyroom.dto.request.CreateRoomRequest;
import com.synclife.studyroom.dto.response.RoomAvailabilityResponse;
import com.synclife.studyroom.dto.response.RoomAvailabilityResponse.TimeSlot;
import com.synclife.studyroom.dto.response.RoomResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request) {
        Room room = Room.create(request.name(), request.location(), request.capacity());
        Room savedRoom = roomRepository.save(room);
        return RoomResponse.from(savedRoom);
    }

    @Transactional(readOnly = true)
    public List<RoomAvailabilityResponse> getRoomAvailability(LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
        List<Room> allRooms = roomRepository.findAll();
        List<RoomAvailabilityResponse> result = new ArrayList<>();

        for (Room room : allRooms) {
            List<Reservation> reservations = reservationRepository.findByRoomAndDateRange(room.getId(), dayStart, dayEnd);

            List<TimeSlot> reservedSlots = new ArrayList<>();
            for (Reservation r : reservations) {
                reservedSlots.add(new TimeSlot(r.getStartAt(), r.getEndAt()));
            }
            reservedSlots.sort((a, b) -> a.startAt().compareTo(b.startAt()));

            List<TimeSlot> availableSlots = new ArrayList<>();
            LocalDateTime current = dayStart;

            for (Reservation r : reservations.stream().sorted((a, b) -> a.getStartAt().compareTo(b.getStartAt())).toList()) {
                if (current.isBefore(r.getStartAt())) {
                    availableSlots.add(new TimeSlot(current, r.getStartAt()));
                }
                current = r.getEndAt().isAfter(current) ? r.getEndAt() : current;
            }

            if (current.isBefore(dayEnd)) {
                availableSlots.add(new TimeSlot(current, dayEnd));
            }

            result.add(new RoomAvailabilityResponse(room.getId(), room.getName(), room.getLocation(),
                room.getCapacity(), date, availableSlots, reservedSlots));
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
            .orElseThrow(() -> new StudyroomException(ErrorCode.ROOM_NOT_FOUND));
    }
}
