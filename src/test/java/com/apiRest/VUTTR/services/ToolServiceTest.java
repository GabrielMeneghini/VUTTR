package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.exceptions.ResourceNotFoundException;
import com.apiRest.VUTTR.repositories.ToolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

    @InjectMocks
    private ToolService toolService;

    @Mock
    private ToolRepository toolRepository;

    @Test
    @DisplayName("Should return all tools when tag is null")
    void findTools_findAll_Scenario01() {
        // Arrange
        List<Tool> tools = List.of(
                new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")),
                new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest")),
                new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("web", "framework", "node", "http2", "https", "localhost"))
        );
        when(toolRepository.findAll()).thenReturn(tools);

        // Act
        var result = toolService.findTools(null);

        // Assert
        verify(toolRepository).findAll();
        verify(toolRepository, never()).findByTag(anyString());
        result.forEach(dto -> assertInstanceOf(ToolDTO.class, dto));
        assertEquals(3, result.size());
        assertEquals("json-server", result.get(1).title());
    }
    @Test
    @DisplayName("Should return only attaching results when tag is specified")
    void findTools_findByTag_Scenario01() {
        // Arrange
        List<Tool> tools = List.of(
                new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest")),
                new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("web", "framework", "node", "http2", "https", "localhost"))
        );
        var tag = "node";
        when(toolRepository.findByTag(tag)).thenReturn(tools);

        // Act
        var result = toolService.findTools(tag);

        // Assert
        verify(toolRepository).findByTag(tag);
        verify(toolRepository, never()).findAll();
        result.forEach(dto -> assertInstanceOf(ToolDTO.class, dto));
        assertEquals(2, result.size());
        assertEquals("fastify", result.get(1).title());
    }

    @Test
    @DisplayName("Should remove duplicate tags")
    void addTool_Scenario01() {
        ToolCreateDTO dto = new ToolCreateDTO(
                "hotel",
                "https://github.com/typicode/hotel",
                "Local app manager. Start apps within your browser, developer tool with local .localhost domain and https out of the box.",
                new ArrayList<>(Arrays.asList("node", "organizing", "webapps", "domain", "developer", "https", "proxy", "Organizing", "TAG", "Tag", "tag", "node")));

        when(toolRepository.save(any(Tool.class))).thenAnswer(a -> {
                Tool t = a.getArgument(0);
                t.setId(1L);
                return t;
        });

        ToolDTO result = toolService.addTool(dto);

        assertEquals(8, result.tags().size());
        assertTrue(result.tags().containsAll(Arrays.asList("node", "organizing", "webapps", "domain", "developer", "https", "proxy", "tag")));
    }

    @Test
    @DisplayName("Should save all the new tags without duplicates")
    void addTagsInTool_Scenario01() {
        List<String> newTags = new ArrayList<>(Arrays.asList("api", "newTag", "Github", "NEWTAG", "NEWtAG2"));
        var tool = new Tool(0L, "json-server", "https://github.com/typicode/json-server"
                , "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges."
                , new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));
        when(toolRepository.findById(tool.getId())).thenReturn(Optional.of(tool));

        toolService.addTagsInTool(newTags, tool.getId());

        assertEquals(8, tool.getTags().size());
        assertTrue(tool.getTags().containsAll(List.of("api", "json", "schema", "node", "github", "rest", "newtag", "newtag2")));
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException if Tool id doesn't exist")
    void addTagsInTool_Scenario02() {
        when(toolRepository.findById(0L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> toolService.addTagsInTool(List.of("newTag"), 0L));
        verifyNoMoreInteractions(toolRepository);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if Tool id doesn't exist")
    void deleteToolById_Scenario01() {
        when(toolRepository.findById(0L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> toolService.deleteToolById(0L));
        verify(toolRepository, never()).deleteById(any());
        verifyNoMoreInteractions(toolRepository);
    }
    @Test
    @DisplayName("Should delete Tool if id exists")
    void deleteToolById_Scenario02() {
        var tool = new Tool(0L, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        when(toolRepository.findById(0L)).thenReturn(Optional.of(tool));

        toolService.deleteToolById(0L);

        verify(toolRepository).deleteById(tool.getId());
        verifyNoMoreInteractions(toolRepository);
    }

    @Test
    @DisplayName("Should delete only the matching tags from the tool")
    void deleteToolTagByName_Scenario01() {
        var toBeDeletedTags = List.of("json", "schema", "api", "github");

        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));

        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));

        toolService.deleteToolTagByName(1L, toBeDeletedTags);

        assertFalse(tool.getTags().containsAll(toBeDeletedTags));
        assertTrue(tool.getTags().containsAll(List.of("node", "rest")));
    }
    @Test
    @DisplayName("Should do nothing when tags to delete don't exist in the tool")
    void deleteToolTagByName_Scenario02() {
        var toBeDeletedTags = new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3"));
        var toolTags = List.of("api", "json", "schema", "node", "github", "rest");

        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                toolTags);

        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));

        toolService.deleteToolTagByName(1L, toBeDeletedTags);

        assertTrue(tool.getTags().containsAll(toolTags));
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException when Tool id doesn't exist")
    void deleteToolTagByName_Scenario03() {
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                toolService.deleteToolTagByName(1L, List.of("api", "json")));
    }

    @Test
    @DisplayName("Should update all tags correctly")
    void updateAllToolTags_Scenario01() {
        var allNewTags = Arrays.asList("tag1", "tag2", "tag3");
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));

        var response = toolService.updateAllToolTags(1L, allNewTags);

        assertTrue(response.tags().containsAll(allNewTags));
        assertFalse(response.tags().containsAll(Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));
    }
    @Test
    @DisplayName("Should remove all duplicate tags")
    void updateAllToolTags_Scenario02() {
        var allNewTags = Arrays.asList("tag1", "tag2", "TAG1", "tag1", "Tag2");
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));

        var response = toolService.updateAllToolTags(1L, allNewTags);

        assertFalse(response.tags().containsAll(allNewTags));
        assertTrue(response.tags().containsAll(Arrays.asList("tag1", "tag2")));
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException if Tool id doesn't exist")
    void updateAllToolTags_Scenario03() {
        var allNewTags = Arrays.asList("tag1", "tag2", "TAG1", "tag1", "Tag2");
        when(toolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> toolService.updateAllToolTags(1L, allNewTags));
    }

}