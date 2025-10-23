package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.repositories.ToolRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        mockMvc.perform(get("/tools"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")
        );
    }
    @Test
    @DisplayName("Should return a list of all Tools available in the database")
    void findTools_findAll_Scenario02() throws Exception {
        createTools();

        mockMvc.perform(get("/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("Notion", "json-server", "fastify"))
        );
    }

    @Test
    @DisplayName("Should return an empty list when there's no Tool in the database with the specified tag")
    void findTools_findByTag_Scenario01() throws Exception {
        createTools();

        mockMvc.perform(get("/tools?tag=nonexistentTag"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]")
        );
    }
    @Test
    @DisplayName("Should return a list of all Tools with the specified tag available in the database")
    void findTools_findByTag_Scenario02() throws Exception {
        createTools();

        mockMvc.perform(get("/tools?tag=node"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", containsInAnyOrder("json-server", "fastify"))
        );
    }
    @ParameterizedTest
    @DisplayName("Should return status 400 Bad Request for invalid tag")
    @ValueSource(strings = {"0", "0123456789012345678901234567890", "@node"})
    void findTools_findByTag_Scenario03_InvalidTags(String tag) throws Exception {
        mockMvc.perform(get("/tools?tag=" + tag))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should save and return the new Tool when the request JSON is valid")
    void addTool_Scenario01() throws Exception {
            mockMvc.perform(post("/tools")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "title": "Duolingo",
                                "link": "https://www.duolingo.com/",
                                "description": "A popular language learning platform offering gamified lessons, exercises, and practice in dozens of languages.",
                                "tags": ["language", "learning", "education", "gamified", "mobile", "web", "courses"]
                            }
                            """))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("Duolingo"))
                    .andExpect(jsonPath("$.link").value("https://www.duolingo.com/"))
                    .andExpect(jsonPath("$.description").value("A popular language learning platform offering gamified lessons, exercises, and practice in dozens of languages."))
                    .andExpect(jsonPath("$.tags", hasSize(7)));
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when JSON has blank fields")
    void addTool_Scenario02() throws Exception {
        mockMvc.perform(post("/tools")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "",
                                "link": "https://www.duolingo.com/",
                                "description": "A popular language learning platform offering gamified lessons, exercises, and practice in dozens of languages.",
                                "tags": ["language", "learning", "education", "gamified", "mobile", "web", "courses"]
                            }
                            """))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when JSON link is invalid")
    void addTool_Scenario03() throws Exception {
        mockMvc.perform(post("/tools")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Duolingo",
                                "link": "duolingo",
                                "description": "A popular language learning platform offering gamified lessons, exercises, and practice in dozens of languages.",
                                "tags": ["language", "learning", "education", "gamified", "mobile", "web", "courses"]
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should save the new tags and return the affected Tool when JSON is valid")
    void addTagsInTool_Scenario01() throws Exception  {
        var tool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));

        mockMvc.perform(post("/tools/" + tool.getId())
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
        mockMvc.perform(post("/tools/1")
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
        mockMvc.perform(post("/tools/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        ["organization", "newTag", "NEWTAG", "Calendar", "newTag2"]
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return status 200 Ok when Tool is successfully deleted")
    void deleteToolById_Scenario01() throws Exception {
        var savedTool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));

        mockMvc.perform(delete("/tools/" + savedTool.getId()))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Should return status 404 Not Found when Tool id doesn't exist")
    void deleteToolById_Scenario02() throws Exception {
        mockMvc.perform(delete("/tools/7"))
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
    @DisplayName("Should return status 200 Ok when successfully updated")
    void updateTool_Scenario01() throws Exception {
        var tool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));

        mockMvc.perform(patch("/tools/" + tool.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"title": "",
                        	"link": "",
                        	"description": "New description"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(tool.getTitle()))
                .andExpect(jsonPath("$.link").value(tool.getLink()))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.description").value(not(tool.getDescription())));
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when TITLE is not unique")
    void updateTool_Scenario02() throws Exception {
        Tool tool1 = new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        Tool tool2 = new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest"));
        Tool savedTool1 = toolRepository.save(tool1);
        toolRepository.save(tool2);

        mockMvc.perform(patch("/tools/" + savedTool1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        	"title": "json-server",
                        	"link": "",
                        	"description": ""
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when LINK is not unique")
    void updateTool_Scenario03() throws Exception {
        Tool tool1 = new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        Tool tool2 = new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest"));
        Tool savedTool1 = toolRepository.save(tool1);
        toolRepository.save(tool2);

        mockMvc.perform(patch("/tools/" + savedTool1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"title": "",
                        	"link": "https://github.com/typicode/json-server",
                        	"description": ""
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when LINK is not valid")
    void updateTool_Scenario04() throws Exception {
        Tool tool1 = new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        Tool savedTool1 = toolRepository.save(tool1);

        mockMvc.perform(patch("/tools/" + savedTool1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"title": "",
                        	"link": "NotValidURL",
                        	"description": ""
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return status 404 Not Found when Tool id doesn't exist")
    void updateTool_Scenario05() throws Exception {
        mockMvc.perform(patch("/tools/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"title": "NewTitle",
                        	"link": "",
                        	"description": ""
                        }
                        """))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Should return status 400 Bad Request when no field was modified")
    void updateTool_Scenario06() throws Exception {
        var tool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));

        mockMvc.perform(patch("/tools/" + tool.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                        	"title": "",
                        	"link": "",
                        	"description": ""
                        }
                        """))
                .andExpect(status().isBadRequest());
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

    private void createTools() {
        Tool tool1 = new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        Tool tool2 = new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest"));
        Tool tool3 = new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("web", "framework", "node", "http2", "https", "localhost"));
        toolRepository.saveAll(Arrays.asList(tool1, tool2, tool3));
    }
}