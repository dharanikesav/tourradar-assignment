package com.assessment;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    public CustomException(Exception e) {
        super(e);
    }

}
