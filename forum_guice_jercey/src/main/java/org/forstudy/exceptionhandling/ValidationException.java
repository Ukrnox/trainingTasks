package org.forstudy.exceptionhandling;

public class ValidationException extends AppException {
    public ValidationException(int status, String message, String developerMessage, String link) {
        super(status, "ValidationException: " + message, developerMessage, link);
    }
}
