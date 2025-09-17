package com.synclife.studyroom.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    protected Room() {}

    public Room(String name, String location, Integer capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    public static Room create(String name, String location, Integer capacity) {
        return new Room(name, location, capacity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCapacity() {
        return capacity;
    }
}
