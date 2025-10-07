package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;

    public List<ToolDTO> findAll(String tag) {
        if(tag==null || tag.isBlank()) {
            return toolRepository.findAll().stream().map(ToolDTO::new).toList();
        } else {
            return toolRepository.findByTag(tag).stream().map(ToolDTO::new).toList();
        }
    }

    public Tool addTool(ToolCreateDTO dto) {
        return toolRepository.save(new Tool(dto));
    }

    public void deleteToolById(Long id) {
        toolRepository.deleteById(id);
    }

}
