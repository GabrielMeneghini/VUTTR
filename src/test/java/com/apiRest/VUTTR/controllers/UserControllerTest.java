package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.entities.User;
import com.apiRest.VUTTR.repositories.UserRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

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
                        	"password": "12345@aB"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully registered."));

        var user = userRepository.findByEmail("emailTeste01@email.com").orElseThrow(() -> new UsernameNotFoundException("User with email \"emailTeste01@email.com\" not found."));
        assertNotNull(user);
        assertNotEquals("12345@aB", user.getPassword());
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
                        	"password": "%s"
                        }
                        """.formatted(password)))
                .andExpect(status().isBadRequest());
    }
    @ParameterizedTest
    @ValueSource(strings = {"emailTeste01@email.com", "email teste", " "})
    @DisplayName("Should return status 400 Bad Request when EMAIL is already registered or is not valid")
    void registerUser_Scenario03(String email) throws Exception {
        userRepository.save(new User(null, "emailTeste01@email.com", "12345@aB"));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"email": "%s",
                        	"password": "12345@aB"
                        }
                        """.formatted(email)))
                .andExpect(status().isBadRequest());
    }

}