package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.UserRegisterDTO;
import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("User successfully registered.");
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> softDeleteAccount(Authentication authentication) {
        var userId = ((User) authentication.getPrincipal()).getId();
        userService.softDeleteAccount(userId);

        return ResponseEntity.noContent().build();
    }

}
