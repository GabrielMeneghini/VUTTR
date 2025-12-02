package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.helpers.KeyLoader;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

    private final Algorithm algorithm;
    public static final String ISSUER = "VUTTR API";

    public TokenService(KeyLoader keyLoader) throws Exception {
        var publicKey = keyLoader.getPublicKey();
        var privateKey = keyLoader.getPrivateKey();
        this.algorithm = Algorithm.RSA256(publicKey, privateKey);
    }

    public String createJwt(User user) {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationTime())
                    .withIssuedAt(Instant.now())
                    .sign(algorithm);
    }

    public String validJwt(String token) {
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
    }

    private Instant expirationTime() {
        return Instant.now().plus(Duration.ofHours(2));
    }

}
