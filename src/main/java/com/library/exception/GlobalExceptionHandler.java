package com.library.exception;

import com.library.dto.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortFieldException(InvalidSortFieldException ex) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.status(409).body(
                new ErrorResponse(409, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleBookNotAvailableException(BookNotAvailableException ex) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException ex) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMemberException(DuplicateMemberException ex) {
        return ResponseEntity.status(409).body(
                new ErrorResponse(409, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(BorrowLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleBorrowLimitExceededException(BorrowLimitExceededException ex) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException ex) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(AlreadyReturnedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyReturnedException(AlreadyReturnedException ex) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(BorrowCountNotZeroException.class)
    public ResponseEntity<ErrorResponse> handleBorrowCountNotZeroException(BorrowCountNotZeroException ex) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(400).body(
                new ErrorResponse(400, message, LocalDateTime.now())
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String message = "Invalid value '" + ex.getValue() +
                "' for parameter '" + ex.getName() + "'";
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, message, LocalDateTime.now())
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(
            HttpMessageNotReadableException ex) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, "Malformed JSON request", LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(
                new ErrorResponse(500, "Something went wrong: " + ex.getMessage(), LocalDateTime.now())
        );
    }
}