package com.apiRest.VUTTR.repositories;

import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.testhelpers.ToolTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ToolTestHelper.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ToolRepositoryTest {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolTestHelper toolTestHelper;

    @Test
    @DisplayName("Should return all Tools when no tag and no title is specified.")
    void findByTagAndTitle_Scenario01() {
        // Arrange
        toolTestHelper.createAndSaveTools();

        // Act
        var result = toolRepository.findByTagAndTitle(PageRequest.of(0, 10), null, null);

        // Assert
        Assertions.assertEquals(10, result.getTotalElements());
    }
    @Test
    @DisplayName("Should return only Tools with specified TAG")
    void findByTagAndTitle_Scenario02() {
        // Arrange
        toolTestHelper.createAndSaveTools();

        // Act
        var result = toolRepository.findByTagAndTitle(PageRequest.of(0, 10), "node", null);

        // Assert
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertTrue(
                result.getContent().stream()
                        .allMatch(t -> t.getTags().contains("node"))
        );
    }
    @Test
    @DisplayName("Should return only Tools with matching TITLE")
    void findByTagAndTitle_Scenario03() {
        // Arrange
        toolTestHelper.createAndSaveTools();

        // Act
        var result = toolRepository.findByTagAndTitle(PageRequest.of(0, 10), null, "sql");

        // Assert
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertTrue(
                result.getContent().stream()
                        .allMatch(t -> t.getTitle().toLowerCase().contains("sql"))
        );
    }
    @Test
    @DisplayName("Should return only Tools with matching TITLE and TAG")
    void findByTagAndTitle_Scenario04() {
        // Arrange
        toolTestHelper.createAndSaveTools();

        // Act
        var result = toolRepository.findByTagAndTitle(PageRequest.of(0, 10), "web", "ti");

        // Assert
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertTrue(result.getContent().stream().allMatch(r -> r.getTitle().contains("ti")));
        Assertions.assertTrue(result.getContent().stream().allMatch(r -> r.getTags().contains("web")));
    }
    @Test
    @DisplayName("Should return correct pagination ordered by title")
    void findByTagAndTitle_Scenario05() {
        // Arrange
        toolTestHelper.createAndSaveTools();

        // Act
        var result = toolRepository.findByTagAndTitle(PageRequest.of(2, 3), null, null);

        // Assert
        Assertions.assertEquals(3, result.getContent().size());
        assertThat(result.getContent())
                .extracting(Tool::getTitle)
                .containsExactly("Notion", "PostgreSQL", "Postman");
    }
    @Test
    @DisplayName("Should return empty page when TAG does not exist")
    void findByTagAndTitle_Scenario06() {
        // Arrange
        toolTestHelper.createAndSaveTools();

        // Act
        var result = toolRepository.findByTagAndTitle(PageRequest.of(0, 10), "nonexistentTag", null);

        // Assert
        Assertions.assertEquals(0, result.getTotalElements());
    }
    @Test
    @DisplayName("Should return empty page when TITLE does not exist")
    void findByTagAndTitle_Scenario07() {
        // Arrange
        toolTestHelper.createAndSaveTools();

        // Act
        var result = toolRepository.findByTagAndTitle(PageRequest.of(0, 10), null, "nonexistentTitle");

        // Assert
        Assertions.assertEquals(0, result.getTotalElements());
    }

}