package com.quary.bookyourinstructor.controller.event.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CreateEventBookLockResponse {

    private final Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private final Instant expirationTime;
}
