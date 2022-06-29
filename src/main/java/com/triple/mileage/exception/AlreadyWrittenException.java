package com.triple.mileage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "User Already have written review at this place")
public class AlreadyWrittenException extends RuntimeException{

    public AlreadyWrittenException() {
    }

    public AlreadyWrittenException(String message) {
        super(message);
    }

    public AlreadyWrittenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyWrittenException(Throwable cause) {
        super(cause);
    }
}
