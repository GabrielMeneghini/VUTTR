package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.dtos.ToolUpdateDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.exceptions.ResourceNotFoundException;
import com.apiRest.VUTTR.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;

    @Transactional(readOnly = true)
    public List<ToolDTO> findTools(String tag) {
        if(tag==null || tag.isBlank()) {
            return toolRepository.findAll().stream().map(ToolDTO::new).toList();
        } else {
            return toolRepository.findByTag(tag).stream().map(ToolDTO::new).toList();
        }
    }

    @Transactional
    public ToolDTO addTool(ToolCreateDTO dto) {
        return new ToolDTO(toolRepository.save(new Tool(dto)));
    }

    @Transactional
    public void deleteToolById(Long id) {
        if(!toolRepository.existsById(id)) {
            throw new ResourceNotFoundException("No tool was found for id " + id + ".");
        }
        toolRepository.deleteById(id);
    }

    @Transactional
    public ToolDTO updateTool(ToolUpdateDTO toolUpdateDTO, Long id) {
        var tool = toolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No tool was found for id " + id + "."));

        if(toolUpdateDTO.title() != null && !toolUpdateDTO.title().isBlank()) {
            tool.setTitle(toolUpdateDTO.title().trim());
        }
        if (toolUpdateDTO.link() != null && !toolUpdateDTO.link().isBlank()) {
            tool.setLink(toolUpdateDTO.link().trim());
        }
        if (toolUpdateDTO.description() != null && !toolUpdateDTO.description().isBlank()) {
            tool.setDescription(toolUpdateDTO.description().trim());
        }

        return new ToolDTO(tool);
    }

}
