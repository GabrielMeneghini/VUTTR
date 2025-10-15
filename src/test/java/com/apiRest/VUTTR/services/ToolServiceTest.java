package com.apiRest.VUTTR.services;

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

import java.util.Arrays;
import java.util.List;

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
    @DisplayName("Should throw ResourceNotFoundException if id doesn't exist")
    void deleteToolById_Scenario01() {
        when(toolRepository.existsById(0L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> toolService.deleteToolById(0L));
        verify(toolRepository, never()).deleteById(any());
        verifyNoMoreInteractions(toolRepository);
    }
    @Test
    @DisplayName("Should delete Tool if id exists")
    void deleteToolById_Scenario02() {
        when(toolRepository.existsById(0L)).thenReturn(true);

        toolService.deleteToolById(0L);

        verify(toolRepository).existsById(0L);
        verify(toolRepository).deleteById(0L);
        verifyNoMoreInteractions(toolRepository);
    }
}