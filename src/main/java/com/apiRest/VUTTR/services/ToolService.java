package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;

    public List<ToolDTO> findAll() {
        return toolRepository.findAll().stream().map(ToolDTO::new).toList();
    }
}
