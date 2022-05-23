package com.quary.bookyourinstructor.controller.event.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class DeclareCyclicEventRequest {

    private String name;
    private String description;
    private String location;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
}
