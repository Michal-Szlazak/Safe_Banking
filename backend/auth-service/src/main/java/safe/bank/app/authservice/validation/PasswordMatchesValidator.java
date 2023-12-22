package safe.bank.app.authservice.validation;

import safe.bank.app.authservice.dtos.UserPostDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserPostDTO userDTO = (UserPostDTO) obj;
        return userDTO.getPassword().equals(userDTO.getConfirmPassword());
    }
}