package com.triple.mileage.exception;

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
