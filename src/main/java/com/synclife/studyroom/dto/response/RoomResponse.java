package com.synclife.studyroom.dto.response;

import com.synclife.studyroom.domain.Room;

public record RoomResponse(
    Long id,
    String name,
    String location,
    int capacity
) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
            room.getId(),
            room.getName(),
            room.getLocation(),
            room.getCapacity()
        );
    }
}
