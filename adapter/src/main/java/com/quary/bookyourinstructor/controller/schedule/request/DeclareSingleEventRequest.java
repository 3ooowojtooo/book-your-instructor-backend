package com.quary.bookyourinstructor.controller.schedule.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class DeclareSingleEventRequest {

    private String name;
    private String description;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    private LocalDateTime dateTime;
}
