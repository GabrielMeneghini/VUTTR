package com.apiRest.VUTTR.dtos;

import com.apiRest.VUTTR.validations.anotations.UniqueEmail;
import jakarta.validation.constraints.*;

public record UserRegisterDTO(@Email
                              @UniqueEmail
                              @NotBlank
                              String email,

                              @Size(min=8,
                                      message = "must contain at least 8 characters.")
                              @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[^\\s]{8,}$",
                                      message = "must contain uppercase, lowercase, number, special character and no spaces.")
                              @NotBlank
                              String password) {
}
