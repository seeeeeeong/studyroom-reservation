package com.synclife.studyroom.service;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import com.synclife.studyroom.domain.Room;
import com.synclife.studyroom.domain.RoomRepository;
import com.synclife.studyroom.dto.request.CreateRoomRequest;
import com.synclife.studyroom.dto.response.RoomResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request) {
        Room room = Room.create(request.name(), request.location(), request.capacity());
        Room savedRoom = roomRepository.save(room);
        return RoomResponse.from(savedRoom);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(RoomResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
            .orElseThrow(() -> new StudyroomException(ErrorCode.ROOM_NOT_FOUND));
    }
}
