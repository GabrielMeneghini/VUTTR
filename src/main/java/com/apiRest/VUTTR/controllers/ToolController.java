package com.apirest.vuttr.controllers;

import com.apirest.vuttr.dtos.ToolCreateDTO;
import com.apirest.vuttr.dtos.ToolDTO;
import com.apirest.vuttr.dtos.ToolUpdateDTO;
import com.apirest.vuttr.services.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tools")
@RequiredArgsConstructor
@Tag(
        name = "Tools",
        description = "Tool management endpoints"
)
public class ToolController {

    private final ToolService toolService;

    @GetMapping
    @Validated
    @Operation(summary = "List tools with pagination")
    public ResponseEntity<List<ToolDTO>> findTools(
            @RequestParam
            @Min(0)
            @Parameter(description = "Page number (zero-based)", example = "0")
            int page,

            @RequestParam
            @Min(1)
            @Parameter(description = "Number of items per page", example = "10")
            int numItems,

            @RequestParam(required = false)
            @Size(min = 2, max = 30)
            @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9]+$",  message = "Tag must contain only letters (including accented ones) and numbers")
            @Parameter(description = "Filter tools by tag name", example = "java")
            String tag,

            @RequestParam(required = false)
            @Size(min = 2, max = 30)
            String title
    ) {
        return ResponseEntity.ok(toolService.findTools(page, numItems, tag, title));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Find tool by ID")
    public ResponseEntity<ToolDTO> findToolById(@PathVariable Long id) {
        return ResponseEntity.ok(toolService.findToolById(id));
    }

    @PostMapping
    @Operation(summary = "Add new tool")
    public ResponseEntity<ToolDTO> addTool(@Valid @RequestBody ToolCreateDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        var createdToolDto = toolService.addTool(dto);

        URI uri = uriComponentsBuilder.path("/tools/{id}")
                .buildAndExpand(createdToolDto.id())
                .toUri();

        return ResponseEntity.created(uri).body(createdToolDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tool by ID")
    public ResponseEntity<Void> deleteToolById(@PathVariable Long id) {
        toolService.deleteToolById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update tool")
    public ResponseEntity<ToolDTO> updateTool(@RequestBody @Valid ToolUpdateDTO toolUpdateDTO, @PathVariable Long id) {
        return ResponseEntity.ok(toolService.updateTool(toolUpdateDTO, id));
    }

}
