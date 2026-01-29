package com.apirest.vuttr.controllers;

import com.apirest.vuttr.dtos.JwtDTO;
import com.apirest.vuttr.dtos.UserLoginDTO;
import com.apirest.vuttr.entities.User;
import com.apirest.vuttr.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "User authentication endpoints"
)
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping
    @SecurityRequirements
    @Operation(summary = "Authenticate user and generate JWT")
    public ResponseEntity<JwtDTO> login(@RequestBody @Valid UserLoginDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        var jwt = tokenService.createJwt(user);

        return ResponseEntity.ok(new JwtDTO(jwt));
    }

}
