package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.repositories.UserRepository;
import com.apiRest.VUTTR.services.TokenService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 200 Ok and correct JWT when fields are valid")
    void login_Scenario01() throws Exception {

        userRepository.save(new User(null, "emailTest@test.com", "$2a$12$ZE7U.fh0.l9UwvVX1rsu1Om6iTq6giMDS6r9Fv/k36Ks4NZC3pyZO"));

        var response = mockMvc.perform(
                    post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "email": "emailTest@test.com",
                                "password": "12345&aB"
                            }
                            """))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String jwt = JsonPath.read(response.getContentAsString(), "$.jwt");
        assertDoesNotThrow(() -> tokenService.validJwt(jwt));
    }
    @ParameterizedTest
    @CsvSource({"emailTest@test.com, 00000000", "wrongEmail@test.com, 12345&aB", "wrongEmail@test.com, 00000000"})
    @DisplayName("Should return 401 Unauthorized when email and/or password are invalid")
    void login_Scenario02(String email, String password) throws Exception {

        userRepository.save(new User(null, "emailTest@test.com", "$2a$12$ZE7U.fh0.l9UwvVX1rsu1Om6iTq6giMDS6r9Fv/k36Ks4NZC3pyZO"));

        var json = """
            {"email":"%s","password":"%s"}
        """.formatted(email, password);

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password.")
                );

    }
    @ParameterizedTest
    @ValueSource(strings = {"", "emailTest"})
    @DisplayName("Should return 400 Bad Request when email is blank or invalid")
    void login_Scenario03(String email) throws Exception {
        mockMvc.perform(
                    post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                            	"email": "%s",
                            	"password": "12345&aB"
                            }
                            """.formatted(email)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return 400 Bad Request when password is blank")
    void login_Scenario04() throws Exception {
        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                	"email": "emailTeste",
                                	"password": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}