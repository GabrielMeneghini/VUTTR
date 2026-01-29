package com.apirest.vuttr.controllers;

import com.apirest.vuttr.dtos.UserRegisterDTO;
import com.apirest.vuttr.dtos.UserUpdatePasswordDTO;
import com.apirest.vuttr.entities.User;
import com.apirest.vuttr.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(
        name = "Users",
        description = "Endpoints for user registration and account management."
)
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @SecurityRequirements
    @Operation(summary = "Register new user")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("User successfully registered.");
    }

    @DeleteMapping("/account")
    @Operation(summary = "Deactivate user account")
    public ResponseEntity<Void> softDeleteAccount(Authentication authentication) {
        var userId = ((User) authentication.getPrincipal()).getId();
        userService.softDeleteAccount(userId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/account/password")
    @Operation(summary = "Update account password")
    public ResponseEntity<String> updateAccountPassword(@RequestBody @Valid UserUpdatePasswordDTO dto, Authentication authentication) {

        var userId = ((User) authentication.getPrincipal()).getId();

        userService.updateAccountPassword(dto, userId);

        return ResponseEntity.ok("Password successfully updated.");
    }

}
