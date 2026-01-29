package com.apirest.vuttr.dtos;

import com.apirest.vuttr.validations.PasswordMatchesInterface;
import com.apirest.vuttr.validations.anotations.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record UserUpdatePasswordDTO (
        @NotBlank
        @Schema(example = "12345@aB", description = "User current password")
        String currentPassword,

        @Size(min=8, message = "must contain at least 8 characters.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[^\\s]{8,}$",
                message = "must contain uppercase, lowercase, number, special character and no spaces.")
        @NotBlank
        @Schema(example = "789&DEfg", description = "User new password")
        String newPassword,

        @NotBlank
        @Schema(example = "789&DEfg", description = "User confirm new password")
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
