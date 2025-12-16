package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.services.ToolTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
@RequiredArgsConstructor
@Tag(
        name = "Tool Tags",
        description = "Endpoints for managing tags associated with tools"
)
public class ToolTagController {

    private final ToolTagService toolTagService;

    @PostMapping("/{id}/tags")
    @Operation(summary = "Add tag(s) to tool")
    public ResponseEntity<ToolDTO> addTagsInTool(@RequestBody @NotEmpty List<String> newTags, @PathVariable Long id) {
        return ResponseEntity.ok(toolTagService.addTagsInTool(newTags, id));
    }

    @DeleteMapping("/{toolId}/tags")
    @Operation(summary = "Delete tool tags by name")
    public ResponseEntity<Void> deleteToolTagByName(@PathVariable Long toolId, @RequestBody @NotEmpty List<String> toBeDeletedTags) {
        toolTagService.deleteToolTagByName(toolId, toBeDeletedTags);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{toolId}/tags")
    @Operation(summary = "Update all tool tags ")
    public ResponseEntity<ToolDTO> updateAllToolTags(@PathVariable Long toolId, @RequestBody @NotEmpty List<String> newTags) {
        return ResponseEntity.ok().body(toolTagService.updateAllToolTags(toolId, newTags));
    }

}
