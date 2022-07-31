package com.quary.bookyourinstructor.controller.booklock.request;

import lombok.Data;

@Data
public class CreateEventBookLockRequest {

    private Integer eventId;
    private Integer eventVersion;
}
