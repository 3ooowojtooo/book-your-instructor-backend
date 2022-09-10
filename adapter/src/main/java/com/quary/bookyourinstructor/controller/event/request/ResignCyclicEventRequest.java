package com.quary.bookyourinstructor.controller.event.request;

import lombok.Data;

@Data
public class ResignCyclicEventRequest {

    private Integer eventId;
    private Integer eventVersion;
}
