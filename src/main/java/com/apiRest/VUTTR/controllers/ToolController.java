package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.dtos.ToolUpdateDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.services.ToolService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @GetMapping
    @Validated
    public ResponseEntity<List<ToolDTO>> findTools(
            @RequestParam(required = false)
            @Size(min = 2, max = 30)
            @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9]+$")
            String tag) {
        return ResponseEntity.ok(toolService.findTools(tag));
    }

    @PostMapping
    public ResponseEntity<ToolDTO> addTool(@Valid @RequestBody ToolCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toolService.addTool(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToolById(@PathVariable Long id) {
        toolService.deleteToolById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ToolDTO> updateTool(@RequestBody @Valid ToolUpdateDTO toolUpdateDTO, @PathVariable Long id) {
        return ResponseEntity.ok(toolService.updateTool(toolUpdateDTO, id));
    }

}
