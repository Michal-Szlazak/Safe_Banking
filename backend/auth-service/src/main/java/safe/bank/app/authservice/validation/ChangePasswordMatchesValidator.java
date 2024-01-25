package safe.bank.app.authservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import safe.bank.app.authservice.dtos.PasswordChangeDTO;

public class ChangePasswordMatchesValidator implements ConstraintValidator<ChangePasswordMatches, Object> {

    @Override
    public void initialize(ChangePasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        PasswordChangeDTO passwordChangeDTO = (PasswordChangeDTO) obj;
        return passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword());
    }
}
