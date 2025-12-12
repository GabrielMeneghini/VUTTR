package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.repositories.UserRepository;
import com.apiRest.VUTTR.services.TokenService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 200 OK and save the user with an encrypted password when data is valid")
    void registerUser_Scenario01() throws Exception {

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"email": "emailTeste01@email.com",
                        	"password": "12345@aB",
                        	"confirmPassword": "12345@aB"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully registered."));

        var user = userRepository.findByEmail("emailTeste01@email.com").orElseThrow(() -> new UsernameNotFoundException("User with email \"emailTeste01@email.com\" not found."));
        assertNotNull(user);
        assertTrue(passwordEncoder.matches("12345@aB", user.getPassword()));
        assertTrue(user.getPassword().startsWith("$2"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"12345@ab", "12345aaB", "abcde@aB", "12345@AB", "2345@aB", " "})
    @DisplayName("Should return status 400 Bad Request when PASSWORD is not valid")
    void registerUser_Scenario02(String password) throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"email": "emailTeste01@email.com",
                        	"password": "%s",
                        	"confirmPassword": "%s"
                        }
                        """.formatted(password, password)))
                .andExpect(status().isBadRequest());
    }
    @ParameterizedTest
    @ValueSource(strings = {"emailTeste01@email.com", "email teste", " "})
    @DisplayName("Should return status 400 Bad Request when EMAIL is already registered or is not valid")
    void registerUser_Scenario03(String email) throws Exception {
        userRepository.save(new User(null, "emailTeste01@email.com", "12345@aB", null));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"email": "%s",
                        	"password": "12345@aB",
                        	"confirmPassword": "12345@aB"
                        }
                        """.formatted(email)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when PASSWORDS do not match")
    void registerUser_Scenario04() throws Exception {

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"email": "emailTeste01@email.com",
                        	"password": "12345@aB",
                        	"confirmPassword": "1234567"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("Passwords do not match"));
    }

    @Test
    @DisplayName("Should return status 204 No Content when account is successfully deleted")
    void softDeleteAccount_Scenario01() throws Exception {

        var user = userRepository.save(new User(null, "emailTeste01@email.com", "$2a$12$Gjj45PL8nrJv0SrutUfR.uVvWt1Llz9LAb25xURO9qzZHYICkYVDy", null));

        var jwt = "Bearer " + tokenService.createJwt(user);

        mockMvc.perform(
                    delete("/users/account")
                    .header("Authorization", jwt))
                .andExpect(status().isNoContent());

        var deletedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(deletedUser.isDeleted());
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when account is already deleted")
    void softDeleteAccount_Scenario02() throws Exception {
        var user = userRepository.save(new User(null, "emailTeste01@email.com", "$2a$12$Gjj45PL8nrJv0SrutUfR.uVvWt1Llz9LAb25xURO9qzZHYICkYVDy", LocalDateTime.now()));

        var jwt = "Bearer " + tokenService.createJwt(user);

        mockMvc.perform(
                    delete("/users/account")
                    .header("Authorization", jwt))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message"). value("User already deleted."));
    }
    @Test
    @DisplayName("Should return 401 Unauthorized when no JWT token is provided")
    void softDeleteAccount_Scenario03() throws Exception {
        mockMvc.perform(delete("/users/account"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication is required to access this resource."));
    }
    @Test
    @DisplayName("Should return 401 Unauthorized when JWT token is invalid")
    void softDeleteAccount_Scenario04() throws Exception {
        mockMvc.perform(delete("/users/account")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("JWT is invalid or expired."));
    }

    @Test
    @DisplayName("Should return 200 Ok and update password when data is valid")
    void updateAccountPassword_Scenario01() throws Exception {
        var user = userRepository.save(new User(null, "emailTeste01@email.com", "$2a$12$pndAUm74Zy0S3X7kHxHx2eNoinSyzqkOcp4DZigAe62gouV6BW6OS", null));

        var jwt = "Bearer " + tokenService.createJwt(user);

        mockMvc.perform(
                    patch("/users/account/password")
                    .header("Authorization", jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                            	"currentPassword": "12345%aB",
                             	"newPassword": "789&DEfg",
                             	"confirmNewPassword": "789&DEfg"
                            }
                            """))
                .andExpect(status().isOk());

        var updatedPassword = userRepository.findByEmail(user.getEmail()).orElseThrow().getPassword();

        assertNotEquals(user.getPassword(), updatedPassword);
        assertTrue(passwordEncoder.matches("12345%aB", user.getPassword()));
        assertTrue(passwordEncoder.matches("789&DEfg", updatedPassword));
    }
    @Test
    @DisplayName("Should return 400 Bad Request when current password is incorrect")
    void updateAccountPassword_Scenario02() throws Exception {
        var user = userRepository.save(new User(null, "emailTeste01@email.com", "$2a$12$pndAUm74Zy0S3X7kHxHx2eNoinSyzqkOcp4DZigAe62gouV6BW6OS", null));

        var jwt = "Bearer " + tokenService.createJwt(user);

        mockMvc.perform(
                        patch("/users/account/password")
                                .header("Authorization", jwt)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                            	"currentPassword": "11111111",
                             	"newPassword": "789&DEfg",
                             	"confirmNewPassword": "789&DEfg"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Current password is incorrect."));
    }
    @Test
    @DisplayName("Should return 400 Bad Request when PASSWORDS do not match")
    void updateAccountPassword_Scenario03() throws Exception {
        var user = userRepository.save(new User(null, "emailTeste01@email.com", "$2a$12$pndAUm74Zy0S3X7kHxHx2eNoinSyzqkOcp4DZigAe62gouV6BW6OS", null));

        var jwt = "Bearer " + tokenService.createJwt(user);

        mockMvc.perform(
                        patch("/users/account/password")
                                .header("Authorization", jwt)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                            	"currentPassword": "12345%aB",
                             	"newPassword": "789&DEfg",
                             	"confirmNewPassword": "11111111"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("Passwords do not match"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"12345@ab", "12345aaB", "abcde@aB", "12345@AB", "2345@aB", " "})
    @DisplayName("Should return status 400 Bad Request when PASSWORD is not valid")
    void updateAccountPassword_Scenario04(String password) throws Exception {
        var user = userRepository.save(new User(null, "emailTeste01@email.com", "$2a$12$k1PFNZH6JJcB/z4GboQWoOhad5e56fKcZT6JlBlUHqZrkRf3ZnCtC", null));

        var jwt = "Bearer " + tokenService.createJwt(user);

        mockMvc.perform(
                        patch("/users/account/password")
                                .header("Authorization", jwt)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "currentPassword": "12345@aB",
                                            "newPassword": "%s",
                                            "confirmNewPassword": "%s"
                                        }
                                """.formatted(password, password)))
                .andExpect(status().isBadRequest());
    }

}