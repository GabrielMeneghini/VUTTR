package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.repositories.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ToolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToolRepository toolRepository;

    @BeforeEach
    void setup() {
        toolRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return an empty list when there's no Tool in the database")
    void findTools_findAll_Scenario01() throws Exception {
        var response = mockMvc.perform(get("/tools"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")
        );
    }

    @Test
    @DisplayName("Should return a list of all Tools available in the database")
    void findTools_findAll_Scenario02() throws Exception {
        Tool tool1 = new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        Tool tool2 = new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest"));
        Tool tool3 = new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("web", "framework", "node", "http2", "https", "localhost"));
        toolRepository.saveAll(Arrays.asList(tool1, tool2, tool3));

        var response = mockMvc.perform(get("/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Notion", "json-server", "fastify"))
        );
    }

}