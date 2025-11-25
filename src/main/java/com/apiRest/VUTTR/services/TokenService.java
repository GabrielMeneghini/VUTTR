package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.helpers.KeyLoader;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public static final String ISSUER = "VUTTR API";

    public TokenService(KeyLoader keyLoader) throws Exception {
        this.publicKey = keyLoader.getPublicKey();
        this.privateKey = keyLoader.getPrivateKey();
    }

    public String createJwt(User user) {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationTime())
                    .withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00")))
                    .sign(algorithm);
    }

    private Instant expirationTime() {
        return LocalDateTime.now()
                .plusHours(2L)
                .toInstant(ZoneOffset.of("-03:00"));
    }

}
