package com.apiRest.VUTTR.repositories;

import com.apiRest.VUTTR.entities.Tool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ToolRepositoryTest {

    @Autowired
    private ToolRepository toolRepository;

    @Test
    @DisplayName("Should return an empty list if no Tools were found with the informed tag")
    void findByTag_Scenario01() {

        // Arrange
        String tag = "nonexistentTag";
        Tool tool1 = new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        Tool tool2 = new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest"));
        Tool tool3 = new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("web", "framework", "node", "http2", "https", "localhost"));
        toolRepository.saveAll(Arrays.asList(tool1, tool2, tool3));

        // Act
        var list = toolRepository.findByTag(tag);

        // Assert
        Assertions.assertTrue(list.isEmpty());

    }

    @Test
    @DisplayName("Should return all the Tools that were found with the informed tag")
    void findByTag_Scenario02() {

        // Arrange
        String tag = "existentTag";
        Tool tool1 = new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "existentTag", "writing", "calendar"));
        Tool tool2 = new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest"));
        Tool tool3 = new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("existentTag", "web", "framework", "node", "http2", "https", "localhost"));
        toolRepository.saveAll(Arrays.asList(tool1, tool2, tool3));

        // Act
        var list = toolRepository.findByTag(tag);

        // Assert
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("Notion", list.get(0).getTitle());
        Assertions.assertEquals("fastify", list.get(1).getTitle());

    }

}