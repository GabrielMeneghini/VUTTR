package com.apirest.vuttr.controllers;

import com.apirest.vuttr.config.AbstractIntegrationTest;
import com.apirest.vuttr.entities.Tool;
import com.apirest.vuttr.repositories.ToolRepository;
import com.apirest.vuttr.testhelpers.ToolTestHelper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ToolControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolTestHelper toolTestHelper;

    @BeforeEach
    void setup() {
        toolRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return specified pagination page")
    void findTools_Scenario01() throws Exception {
        toolTestHelper.createAndSaveTools();

        var page = 1;
        var numItems = 6;

        mockMvc.perform(get("/tools")
                        .param("page", String.valueOf(page))
                        .param("numItems", String.valueOf(numItems)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].title").value("Notion"))
                .andExpect(jsonPath("$[1].title").value("PostgreSQL"))
                .andExpect(jsonPath("$[2].title").value("Postman"))
                .andExpect(jsonPath("$[3].title").value("Visual Studio Code"));
    }
    @Test
    @DisplayName("Should return specified number of items in pagination page")
    void findTools_Scenario02() throws Exception {
        toolTestHelper.createAndSaveTools();

        var page = 0;
        var numItems = 3;

        mockMvc.perform(get("/tools")
                        .param("page", String.valueOf(page))
                        .param("numItems", String.valueOf(numItems)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].title", contains("Docker", "fastify", "GitHub")));
    }
    @Test
    @DisplayName("Should return 400 Bad Request when \"page\" number less than 0")
    void findTools_Scenario03() throws Exception {
        var page = -4;
        var numItems = 3;

        mockMvc.perform(get("/tools")
                        .param("page", String.valueOf(page))
                        .param("numItems", String.valueOf(numItems)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return 400 Bad Request when \"numItems\" number less than 1")
    void findTools_Scenario04() throws Exception {
        var page = 1;
        var numItems = 0;

        mockMvc.perform(get("/tools")
                        .param("page", String.valueOf(page))
                        .param("numItems", String.valueOf(numItems)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return all tags of each tool")
    void findTools_Scenario05() throws Exception {
        toolTestHelper.createAndSaveTools();

        var page = 0;
        var numItems = 3;

        mockMvc.perform(get("/tools")
                    .param("page", String.valueOf(page))
                    .param("numItems", String.valueOf(numItems)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[?(@.title=='Docker')].tags.length()").value(4))
                .andExpect(jsonPath("$[?(@.title=='fastify')].tags.length()").value(7))
                .andExpect(jsonPath("$[?(@.title=='GitHub')].tags.length()").value(4));
    }
    @Test
    @DisplayName("Should return an empty list when there's no Tool in the database with the specified tag")
    void findTools_Scenario06() throws Exception {
        toolTestHelper.createAndSaveTools();

        var page = 0;
        var numItems = 10;
        var tag = "nonexistentTag";

        mockMvc.perform(get("/tools")
                    .param("page", String.valueOf(page))
                    .param("numItems", String.valueOf(numItems))
                    .param("tag", String.valueOf(tag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))
        );
    }
    @Test
    @DisplayName("Should return a list of all Tools with the specified tag available in the database")
    void findTools_Scenario07() throws Exception {
        toolTestHelper.createAndSaveTools();

        var page = 0;
        var numItems = 10;
        var tag = "node";

        mockMvc.perform(get("/tools")
                        .param("page", String.valueOf(page))
                        .param("numItems", String.valueOf(numItems))
                        .param("tag", tag))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title", contains("fastify", "json-server"))
        );
    }
    @ParameterizedTest
    @DisplayName("Should return status 400 Bad Request for invalid tag")
    @ValueSource(strings = {"0", "0123456789012345678901234567890", "@node"})
    void findTools_Scenario08_InvalidTags(String tag) throws Exception {
        mockMvc.perform(get("/tools?tag=" + tag))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Should return specified number of items in pagination page when searching by tag")
    void findTools_Scenario09() throws Exception {
        toolTestHelper.createAndSaveTools();
        toolTestHelper.createAndSaveTools();

        var page = 0;
        var numItems = 3;
        var tag = "node";

        mockMvc.perform(get("/tools")
                        .param("page", String.valueOf(page))
                        .param("numItems", String.valueOf(numItems))
                        .param("tag", tag))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].title", contains("fastify", "fastify", "json-server"))
        );
    }

    @Test
    @DisplayName("Should return Tool of correct id")
    void findToolById_Scenario01() throws Exception {
        var toolList = toolTestHelper.createAndSaveTools();
        var toolToBeFound = toolList.get(2);

        mockMvc.perform(get("/tools/" + toolToBeFound.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(toolToBeFound.getTitle()));
    }
    @Test
    @DisplayName("Should return status 404 Not Found when Tool id doesn't exist")
    void findToolById_Scenario02() throws Exception {
        mockMvc.perform(get("/tools/0"))
                .andExpect(status().isNotFound());
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
    @DisplayName("Should return URI in Location Header")
    void addTool_Scenario04() throws Exception {
        var response = mockMvc.perform(post("/tools")
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
                .andReturn().getResponse();

        var headerLocation = response.getHeader("Location");
        var returnedId = JsonPath.read(response.getContentAsString(), "$.id");
        assertNotNull(headerLocation,
                "Location header should not be null");
        assertTrue(headerLocation.endsWith("/tools/" + returnedId),
                "Location header should end with /tools/" + returnedId);
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
    @DisplayName("Should return status 200 Ok when successfully updated")
    void updateTool_Scenario01() throws Exception {
        var tool = toolRepository.save(new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));
        var oldTitle = tool.getTitle();
        var oldLink = tool.getLink();
        var oldDescription = tool.getDescription();

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
                .andExpect(jsonPath("$.title").value(oldTitle))
                .andExpect(jsonPath("$.link").value(oldLink))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.description").value(not(oldDescription)));

        var updatedTool = toolRepository.findById(tool.getId()).orElseThrow();
        assertEquals("New description", updatedTool.getDescription());
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

}