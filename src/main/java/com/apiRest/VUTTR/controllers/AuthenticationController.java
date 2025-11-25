package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.JwtDTO;
import com.apiRest.VUTTR.dtos.UserLoginDTO;
import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.services.TokenService;
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
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<JwtDTO> login(@RequestBody @Valid UserLoginDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        var jwt = tokenService.createJwt(user);

        return ResponseEntity.ok(new JwtDTO(jwt));
    }

}
