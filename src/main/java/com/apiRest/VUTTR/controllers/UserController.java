package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.UserRegisterDTO;
import com.apiRest.VUTTR.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
