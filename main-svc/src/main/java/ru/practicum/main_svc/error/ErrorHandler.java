package ru.practicum.main_svc.error;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main_svc.error.exception.CategoryConflictException;
import ru.practicum.main_svc.error.exception.DuplicationNameException;
import ru.practicum.main_svc.error.exception.NotFoundException;
import ru.practicum.main_svc.error.exception.RequestConflictException;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(final Exception e) {
        return new ErrorResponse(
                e.getMessage(),
                "Fail Validation",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse(
                e.getMessage(),
                "Not found content",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler({CategoryConflictException.class, DuplicationNameException.class, RequestConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoryConflictException(final Exception e) {
        return new ErrorResponse(
                e.getMessage(),
                "Conflict request data",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleThrowable(final Throwable e) {
//        return new ErrorResponse(e.getMessage());
//    }
}
