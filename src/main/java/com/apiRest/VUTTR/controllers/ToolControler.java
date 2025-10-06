package com.apiRest.VUTTR.controllers;

import com.apiRest.VUTTR.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tools")
public class ToolControler {

    @Autowired
    private ToolService toolService;

}
