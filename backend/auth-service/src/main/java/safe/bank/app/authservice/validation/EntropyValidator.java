package safe.bank.app.authservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EntropyValidator implements ConstraintValidator<EntropyValidation, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        double entropy = calculateEntropy(password);
        return entropy >= 60;
    }

    private double calculateEntropy(String password) {
        int poolSize = calculatePoolSize(password);
        return Math.log(Math.pow(poolSize, password.length())) / Math.log(2);
    }

    private int calculatePoolSize(String password) {
        int pool = 0;

        if (password.matches(".*[a-z].*")) {
            pool += 26;
        }
        if (password.matches(".*[A-Z].*")) {
            pool += 26;
        }
        if (password.matches(".*\\d.*")) {
            pool += 10;
        }
        if (password.matches(".*[!@#$%^&*()-_=+\\[\\]{}|;:,.<>?].*")) {
            pool += 26;
        }

        return pool;
    }
}

