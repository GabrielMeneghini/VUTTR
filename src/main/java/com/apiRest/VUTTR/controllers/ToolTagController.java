package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.services.ToolTagService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolTagController {

    @Autowired
    private ToolTagService toolTagService;

    @PostMapping("/{id}/tags")
    public ResponseEntity<ToolDTO> addTagsInTool(@RequestBody @NotEmpty List<String> newTags, @PathVariable Long id) {
        return ResponseEntity.ok(toolTagService.addTagsInTool(newTags, id));
    }

    @DeleteMapping("/{toolId}/tags")
    public ResponseEntity<Void> deleteToolTagByName(@PathVariable Long toolId, @RequestBody @NotEmpty List<String> toBeDeletedTags) {
        toolTagService.deleteToolTagByName(toolId, toBeDeletedTags);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{toolId}/tags")
    public ResponseEntity<ToolDTO> updateAllToolTags(@PathVariable Long toolId, @RequestBody @NotEmpty List<String> newTags) {
        return ResponseEntity.ok().body(toolTagService.updateAllToolTags(toolId, newTags));
    }

}
