package com.synclife.studyroom.controller;

import com.synclife.studyroom.dto.request.CreateRoomRequest;
import com.synclife.studyroom.dto.response.RoomResponse;
import com.synclife.studyroom.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/rooms")
    public RoomResponse createRoom(@RequestBody CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @GetMapping("/rooms")
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }
}
