package ru.practicum.main_svc.error.exception;

public class DuplicationNameException extends RuntimeException {
    public DuplicationNameException(String message) {
        super(message);
    }
}
