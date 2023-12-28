package safe.bank.app.authservice.controller_advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import safe.bank.app.authservice.controller_advice.exceptions.UserCreationException;

@ControllerAdvice
public class UserCreationHandler {

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ApiError> handleCustomException(UserCreationException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Failed to create user.", ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
