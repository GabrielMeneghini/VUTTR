package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.dtos.ToolUpdateDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.exceptions.NoUpdateDetectedException;
import com.apiRest.VUTTR.exceptions.ResourceNotFoundException;
import com.apiRest.VUTTR.helpers.ToolHelper;
import com.apiRest.VUTTR.repositories.ToolRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Mock
    private ToolHelper toolHelper;

    @Test
    @DisplayName("Should return all tools when tag is null")
    void findTools_findAll_Scenario01() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        List<Tool> tools = List.of(
                new Tool(null, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar")),
                new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest")),
                new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("web", "framework", "node", "http2", "https", "localhost"))
        );
        Page<Tool> pageTools = new PageImpl<>(tools, pageable, tools.size());
        when(toolRepository.findAll(pageable)).thenReturn(pageTools);

        // Act
        var result = toolService.findTools(0, 10, null);

        // Assert
        verify(toolRepository).findAll(pageable);
        verify(toolRepository, never()).findByTag(any(Pageable.class),anyString());
        result.forEach(dto -> assertInstanceOf(ToolDTO.class, dto));
        assertEquals(3, result.size());
        assertEquals("json-server", result.get(1).title());
    }
    @Test
    @DisplayName("Should return only attaching results when tag is specified")
    void findTools_findByTag_Scenario01() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        List<Tool> tools = List.of(
                new Tool(null, "json-server", "https://github.com/typicode/json-server", "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.", Arrays.asList("api", "json", "schema", "node", "github", "rest")),
                new Tool(null, "fastify", "https://www.fastify.io/", "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.", Arrays.asList("web", "framework", "node", "http2", "https", "localhost"))
        );
        var tag = "node";
        when(toolRepository.findByTag(pageable, tag)).thenReturn(tools);

        // Act
        var result = toolService.findTools(0, 10, tag);

        // Assert
        verify(toolRepository).findByTag(pageable, tag);
        verify(toolRepository, never()).findAll(any(Pageable.class));
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
    @DisplayName("Should throw ResourceNotFoundException if Tool id doesn't exist")
    void deleteToolById_Scenario01() {
        var id = 0L;
        when(toolHelper.validateToolExists(id)).thenThrow(new ResourceNotFoundException("No tool was found for id " + id + "."));

        assertThrows(ResourceNotFoundException.class, () -> toolService.deleteToolById(id));
        verify(toolRepository, never()).deleteById(any());
    }
    @Test
    @DisplayName("Should delete Tool if id exists")
    void deleteToolById_Scenario02() {
        var tool = new Tool(0L, "Notion", "https://notion.so", "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.", Arrays.asList("organization", "planning", "collaboration", "writing", "calendar"));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        toolService.deleteToolById(0L);

        verify(toolRepository).deleteById(tool.getId());
    }

    @Test
    @DisplayName("Should not replace null/blank fields")
    void updateTool_Scenario01() {
        var toolUpdateDTO = new ToolUpdateDTO(null, null, "new description");
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        var updatedTool = toolService.updateTool(toolUpdateDTO, tool.getId());

        assertAll(
            () -> assertEquals("Notion", updatedTool.title()),
            () -> assertEquals("https://notion.so", updatedTool.link()),
            () -> assertEquals(toolUpdateDTO.description(), updatedTool.description())
        );
        verify(toolHelper).validateToolExists(tool.getId());
    }
    @Test
    @DisplayName("Should throw NoUpdateDetectedException when all fields are BLANK")
    void updateTool_Scenario02() {
        var toolUpdateDTO = new ToolUpdateDTO("", "", "");
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        assertThrows(NoUpdateDetectedException.class, () -> toolService.updateTool(toolUpdateDTO, tool.getId()));
        assertEquals(tool.getTitle(), "Notion");
        assertEquals(tool.getLink(), "https://notion.so");
        assertEquals(tool.getDescription(), "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.");
    }
    @Test
    @DisplayName("Should throw NoUpdateDetectedException when all fields are NULL")
    void updateTool_Scenario03() {
        var toolUpdateDTO = new ToolUpdateDTO(null, null, null);
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        assertThrows(NoUpdateDetectedException.class, () -> toolService.updateTool(toolUpdateDTO, tool.getId()));
        assertEquals(tool.getTitle(), "Notion");
        assertEquals(tool.getLink(), "https://notion.so");
        assertEquals(tool.getDescription(), "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.");
    }
    @Test
    @DisplayName("Should throw ResourceNotFoundException when Tool id doesn't exist")
    void updateTool_Scenario04() {
        var toolUpdateDTO = new ToolUpdateDTO(null, null, "new description");
        var id = 0L;
        when(toolHelper.validateToolExists(id)).thenThrow(new ResourceNotFoundException("No tool was found for id " + id + "."));


        assertThrows(ResourceNotFoundException.class, () -> toolService.updateTool(toolUpdateDTO, id));
    }
    @Test
    @DisplayName("Should trim fields before updating")
    void updateTool_Scenario05() {
        var toolUpdateDTO = new ToolUpdateDTO("     new Title", "  new  Link    ", "new description    ");
        var tool = new Tool(1L, "Notion", "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                new ArrayList<>(Arrays.asList("api", "json", "schema", "node", "github", "rest")));
        when(toolHelper.validateToolExists(tool.getId())).thenReturn(tool);

        var updatedTool = toolService.updateTool(toolUpdateDTO, tool.getId());

        assertAll(
            () -> assertEquals("new Title", updatedTool.title()),
            () -> assertEquals("new  Link", updatedTool.link()),
            () -> assertEquals("new description", updatedTool.description())
        );
    }

}