package safe.bank.app.bankservice.exception_handling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {

    private String message;
}
