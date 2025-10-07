package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ToolDTO>> findAll(@RequestParam(required = false) String tag) {
        return ResponseEntity.ok(toolService.findAll(tag));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Tool> addTool(@RequestBody ToolCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toolService.addTool(dto));
    }

}
