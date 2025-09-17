package com.synclife.studyroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRoomRequest(
    @NotBlank
    String name,

    @NotBlank
    String location,

    @NotNull
    @Positive
    int capacity
) {}
