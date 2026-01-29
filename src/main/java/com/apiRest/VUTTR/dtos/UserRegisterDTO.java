package com.apirest.vuttr.dtos;

import com.apirest.vuttr.validations.PasswordMatchesInterface;
import com.apirest.vuttr.validations.anotations.PasswordMatches;
import com.apirest.vuttr.validations.anotations.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record UserRegisterDTO(
        @Email
        @UniqueEmail
        @NotBlank
        @Schema(example = "emailTeste1@email.com", description = "User email")
        String email,

        @Size(min=8, message = "must contain at least 8 characters.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[^\\s]{8,}$",
                  message = "must contain uppercase, lowercase, number, special character and no spaces.")
        @NotBlank
        @Schema(example = "12345@aB", description = "User password")
        String password,

        @NotBlank
        @Schema(example = "12345@aB", description = "Confirm password")
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