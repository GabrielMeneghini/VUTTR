package com.apirest.vuttr.services;

import com.apirest.vuttr.dtos.ToolCreateDTO;
import com.apirest.vuttr.dtos.ToolDTO;
import com.apirest.vuttr.dtos.ToolUpdateDTO;
import com.apirest.vuttr.entities.Tool;
import com.apirest.vuttr.exceptions.NoUpdateDetectedException;
import com.apirest.vuttr.exceptions.ResourceNotFoundException;
import com.apirest.vuttr.helpers.ToolHelper;
import com.apirest.vuttr.repositories.ToolRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
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

    @Mock
    private ToolHelper toolHelper;

    @Test
    @DisplayName("Should convert Tools to DTO")
    void findTools_Scenario01() {
        // Arrange
        Tool tool = new Tool();
        tool.setTitle("Docker");
        Page<Tool> page = new PageImpl<>(List.of(tool));
        when(toolRepository.findByTagAndTitle(any(), any(), any()))
                .thenReturn(page);

        // Act
        List<ToolDTO> result = toolService.findTools(0, 10, null, null);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Docker", result.get(0).title());
    }
    @Test
    @DisplayName("Should return empty list when repository returns empty page")
    void findTools_Scenario02() {
        // Arrange
        when(toolRepository.findByTagAndTitle(any(), any(), any())).thenReturn(Page.empty());

        // Act
        List<ToolDTO> result = toolService.findTools(0, 10, null, null);

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }
    @Test
    @DisplayName("Should preserve repository order when mapping Tool to ToolDTO")
    void findTools_Scenario03() {
        // Arrange
        Tool t1 = new Tool(); t1.setTitle("ATool1");
        Tool t2 = new Tool(); t2.setTitle("BTool2");
        Tool t3 = new Tool(); t3.setTitle("CTool3");
        Page<Tool> page = new PageImpl<>(List.of(t1, t2, t3));

        when(toolRepository.findByTagAndTitle(any(), any(), any())).thenReturn(page);

        // Act
        List<ToolDTO> result = toolService.findTools(0, 10, null, null);

        // Assert
        Assertions.assertEquals("ATool1", result.get(0).title());
        Assertions.assertEquals("BTool2", result.get(1).title());
        Assertions.assertEquals("CTool3", result.get(2).title());
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