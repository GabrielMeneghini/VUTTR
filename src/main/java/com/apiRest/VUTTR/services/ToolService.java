package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;

}
