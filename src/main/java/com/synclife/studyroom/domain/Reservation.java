package com.synclife.studyroom.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    protected Reservation() {}

    public Reservation(Room room, String userId, LocalDateTime startAt, LocalDateTime endAt) {
        this.room = room;
        this.userId = userId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static Reservation create(Room room, String userId, LocalDateTime startAt, LocalDateTime endAt) {
        return new Reservation(room, userId, startAt, endAt);
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public boolean isOwnedBy(String userId) {
        return this.userId.equals(userId);
    }
}
