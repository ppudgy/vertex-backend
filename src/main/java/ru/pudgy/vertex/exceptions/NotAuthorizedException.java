package ru.pudgy.vertex.exceptions;

public class NotAuthorizedException extends BaseException {

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}