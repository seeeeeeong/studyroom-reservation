package com.synclife.studyroom.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
        "WHERE r.room.id = :roomId " +
        "AND r.startAt < :dayEnd " +
        "AND r.endAt > :dayStart " +
        "ORDER BY r.startAt")
    List<Reservation> findByRoomAndDateRange(@Param("roomId") Long roomId,
        @Param("dayStart") LocalDateTime dayStart,
        @Param("dayEnd") LocalDateTime dayEnd);
}
