package safe.bank.app.bankservice.exception_handling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ApiError> handleApiError(ResponseStatusException ex) {
        return new ResponseEntity<>(
                new ApiError(ex.getMessage()),
                ex.getStatusCode()
        );
    }

}
