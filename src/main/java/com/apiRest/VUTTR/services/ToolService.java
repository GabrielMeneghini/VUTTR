package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.dtos.ToolUpdateDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.exceptions.NoUpdateDetectedException;
import com.apiRest.VUTTR.helpers.ToolHelper;
import com.apiRest.VUTTR.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.apiRest.VUTTR.helpers.ToolHelper.removeDuplicateTags;
import static com.apiRest.VUTTR.helpers.ToolHelper.updateFieldIfPresent;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolHelper toolHelper;

    @Transactional(readOnly = true)
    public List<ToolDTO> findTools(int page, int numItems, String tag) {
        if(tag==null || tag.isBlank()) {
            return toolRepository.findAll(PageRequest.of(page, numItems)).stream().map(ToolDTO::new).toList();
        } else {
            return toolRepository.findByTag(PageRequest.of(page, numItems), tag).stream().map(ToolDTO::new).toList();
        }
    }

    @Transactional(readOnly = true)
    public ToolDTO findToolById(Long id) {
        return new ToolDTO(toolHelper.validateToolExists(id));
    }

    @Transactional
    public ToolDTO addTool(ToolCreateDTO dto) {
        var tool = new Tool(dto);
        removeDuplicateTags(tool);

        return new ToolDTO(toolRepository.save(tool));
    }

    @Transactional
    public void deleteToolById(Long id) {
        Tool tool = toolHelper.validateToolExists(id);

        toolRepository.deleteById(tool.getId());
    }

    @Transactional
    public ToolDTO updateTool(ToolUpdateDTO toolUpdateDTO, Long id) {
        Tool tool = toolHelper.validateToolExists(id);

        // Updates all non-null/non-blank fields and ensures at least one field was modified
        int countUpdates = 0;
        countUpdates += updateFieldIfPresent(toolUpdateDTO.title(), tool::setTitle);
        countUpdates += updateFieldIfPresent(toolUpdateDTO.link(), tool::setLink);
        countUpdates += updateFieldIfPresent(toolUpdateDTO.description(), tool::setDescription);
        if(countUpdates==0) {
            throw new NoUpdateDetectedException("Must update at least one field");
        }

        return new ToolDTO(tool);
    }

}