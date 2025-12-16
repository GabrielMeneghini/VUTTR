package com.apiRest.VUTTR.dtos;

import com.apiRest.VUTTR.validations.PasswordMatchesInterface;
import com.apiRest.VUTTR.validations.anotations.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record UserUpdatePasswordDTO (
        @NotBlank
        String currentPassword,

        @Size(min=8, message = "must contain at least 8 characters.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[^\\s]{8,}$",
                message = "must contain uppercase, lowercase, number, special character and no spaces.")
        @NotBlank
        String newPassword,

        @NotBlank
        String confirmNewPassword
) implements PasswordMatchesInterface {
        @Override
        @JsonIgnore
        public String getPassword() {
            return this.newPassword;
        }

        @Override
        @JsonIgnore
        public String getConfirmPassword() {
            return this.confirmNewPassword;
        }
}
