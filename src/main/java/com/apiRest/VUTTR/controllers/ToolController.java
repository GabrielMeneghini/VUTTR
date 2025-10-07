package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @GetMapping
    public List<ToolDTO> findAll(@RequestParam(required = false) String tag) {
        return toolService.findAll(tag);
    }

}
