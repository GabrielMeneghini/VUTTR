package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.exceptions.ResourceNotFoundException;
import com.apiRest.VUTTR.helpers.ToolHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToolTagServiceTest {

    @InjectMocks
    private ToolTagService toolTagService;

    @Mock
    private ToolHelper toolHelper;

    @Test
    @DisplayName("Should save all the new tags without duplicates")
    void addTagsInTool_Scenario01() {
        List<String> newTags = new ArrayList<>(Arrays.asList("api", "newTag", "Github", "NEWTAG", "NEWtAG2"));
        var tool = new Tool(0L, "json-server", "https://github.com/typicode/json-server"
                , "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges."
                , new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        toolTagService.addTagsInTool(newTags, tool.getId());

        assertEquals(8, tool.getTags().size());
        assertTrue(tool.getTags().containsAll(List.of("api", "json", "schema", "node", "github", "rest", "newtag", "newtag2")));
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException if Tool id doesn't exist")
    void addTagsInTool_Scenario02() {
        var id = 0L;
        when(toolHelper.validateToolExists(id)).thenThrow(new ResourceNotFoundException("No tool was found for id " + id + "."));

        assertThrows(ResourceNotFoundException.class, () -> toolTagService.addTagsInTool(List.of("newTag"), id));
    }

    @Test
    @DisplayName("Should delete only the matching tags from the tool")
    void deleteToolTagByName_Scenario01() {
        var toBeDeletedTags = List.of("json", "schema", "api", "github");

        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));

        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        toolTagService.deleteToolTagByName(1L, toBeDeletedTags);

        assertFalse(tool.getTags().containsAll(toBeDeletedTags));
        assertTrue(tool.getTags().containsAll(List.of("node", "rest")));
    }
    @Test
    @DisplayName("Should do nothing when tags to delete don't exist in the tool")
    void deleteToolTagByName_Scenario02() {
        var toBeDeletedTags = new ArrayList<>(Arrays.asList("tag1", "tag2", "tag3"));
        var toolTags = new ArrayList<>(List.of("api", "json", "schema", "node", "github", "rest"));

        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                toolTags);

        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        toolTagService.deleteToolTagByName(1L, toBeDeletedTags);

        assertTrue(tool.getTags().containsAll(toolTags));
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException when Tool id doesn't exist")
    void deleteToolTagByName_Scenario03() {
        var id = 1L;
        when(toolHelper.validateToolExists(id)).thenThrow(new ResourceNotFoundException("No tool was found for id " + id + "."));

        assertThrows(ResourceNotFoundException.class, () ->
                toolTagService.deleteToolTagByName(id, List.of("api", "json")));
    }

    @Test
    @DisplayName("Should update all tags correctly")
    void updateAllToolTags_Scenario01() {
        var allNewTags = Arrays.asList("tag1", "tag2", "tag3");
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        var response = toolTagService.updateAllToolTags(1L, allNewTags);

        assertTrue(response.tags().containsAll(allNewTags));
        assertFalse(response.tags().containsAll(Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")));
    }
    @Test
    @DisplayName("Should remove all duplicate, blank and null tags")
    void updateAllToolTags_Scenario02() {
        var allNewTags = Arrays.asList(" tag1", "tag2 ", "TAG1", "tag1", "Tag2", "", null);
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        var response = toolTagService.updateAllToolTags(1L, allNewTags);

        assertFalse(response.tags().containsAll(allNewTags));
        assertEquals(Set.of("tag1", "tag2"), new HashSet<>(response.tags()));
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException if Tool id doesn't exist")
    void updateAllToolTags_Scenario03() {
        var allNewTags = Arrays.asList("tag1", "tag2", "TAG1", "tag1", "Tag2");
        var id = 1L;
        when(toolHelper.validateToolExists(id)).thenThrow(new ResourceNotFoundException("No tool was found for id " + id + "."));

        assertThrows(ResourceNotFoundException.class, () -> toolTagService.updateAllToolTags(id, allNewTags));
    }

}
