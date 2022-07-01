package com.quary.bookyourinstructor.model.event.exception;

import com.quary.bookyourinstructor.model.exception.BaseInvalidInputException;

public class ConcurrentDataModificationException extends BaseInvalidInputException {

    public ConcurrentDataModificationException() {
        super(EventErrorCode.CONCURRENT_DATA_MODIFICATION_EXCEPTION, "Data has been concurrently modified");
    }
}
