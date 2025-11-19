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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ToolTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToolRepository toolRepository;

    @BeforeEach
    void setup() {
        toolRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save the new tags and return the affected Tool when JSON is valid")
    void addTagsInTool_Scenario01() throws Exception  {
        var tool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));

        mockMvc.perform(post("/tools/" + tool.getId() + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        ["organization", "newTag", "NEWTAG", "Calendar", "newTag2"]
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Notion"))
                .andExpect(jsonPath("$.tags", contains(
                        "organization",
                        "planning",
                        "collaboration",
                        "writing",
                        "calendar",
                        "newtag",
                        "newtag2")));
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when JSON is empty")
    void addTagsInTool_Scenario02() throws Exception {
        mockMvc.perform(post("/tools/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        []
                        """))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 404 Not Found when Tool id doesn't exist")
    void addTagsInTool_Scenario03() throws Exception {
        long nonExistentId = toolRepository.count() + 1;
        mockMvc.perform(post("/tools/" + nonExistentId + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        ["organization", "newTag", "NEWTAG", "Calendar", "newTag2"]
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return status 204 No Content when tags were successfully deleted")
    void deleteToolTagByName_Scenario01() throws Exception {
        var tool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));

        mockMvc.perform(delete("/tools/" + tool.getId() + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"planning\", \"writing\"]"))
                .andExpect(status().isNoContent());

        var updatedTool = toolRepository.findById(tool.getId()).get();
        assertFalse(updatedTool.getTags().containsAll(Arrays.asList("planning", "writing")));
        assertTrue(updatedTool.getTags().containsAll(Arrays.asList("organization", "collaboration", "calendar")));
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when JSON is empty")
    void deleteToolTagByName_Scenario02() throws Exception {
        mockMvc.perform(delete("/tools/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 404 Not Found when Tool id doesn't exist")
    void deleteToolTagByName_Scenario03() throws Exception {
        mockMvc.perform(delete("/tools/" + toolRepository.count()+1 + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"planning\", \"writing\"]"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return status 200 Ok when JSON and Tool id are valid")
    void updateAllToolTags_Scenario01() throws Exception {
        var tool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));

        mockMvc.perform(put("/tools/" + tool.getId() + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"tag1\", \"tag2\", \"tag3\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags", contains("tag1", "tag2", "tag3")))
                .andExpect(jsonPath("$.tags", not(hasItems("organization", "planning", "collaboration", "writing", "calendar"))));
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when JSON is empty")
    void updateAllToolTags_Scenario02() throws Exception {
        mockMvc.perform(put("/tools/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 404 Not Found when Tool id doesn't exist")
    void updateAllToolTags_Scenario03() throws Exception {
        mockMvc.perform(put("/tools/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"tag1\", \"tag2\", \"tag3\"]"))
                .andExpect(status().isNotFound());
    }
}
