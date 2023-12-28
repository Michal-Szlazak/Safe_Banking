package safe.bank.app.authservice.controller_advice.exceptions;

public class UserCreationException extends Exception {

    public UserCreationException(String error) {
        super(error);
    }
}
