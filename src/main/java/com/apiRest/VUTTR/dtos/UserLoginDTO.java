package com.apirest.vuttr.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(@Email
                           @NotBlank
                           @Schema(example = "emailTeste1@email.com", description = "User email")
                           String email,

                           @NotBlank
                           @Schema(example = "12345@aB", description = "User password")
                           String password) {
}
