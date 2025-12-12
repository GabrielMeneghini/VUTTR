package com.apiRest.VUTTR.dtos;

import com.apiRest.VUTTR.validations.PasswordMatchesInterface;
import com.apiRest.VUTTR.validations.anotations.PasswordMatches;
import com.apiRest.VUTTR.validations.anotations.UniqueEmail;
import jakarta.validation.constraints.*;

@PasswordMatches
public record UserRegisterDTO(
        @Email
        @UniqueEmail
        @NotBlank
        String email,

        @Size(min=8, message = "must contain at least 8 characters.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[^\\s]{8,}$",
                  message = "must contain uppercase, lowercase, number, special character and no spaces.")
        @NotBlank
        String password,

        @NotBlank
        String confirmPassword
) implements PasswordMatchesInterface {
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getConfirmPassword() {
        return this.confirmPassword;
    }
}
