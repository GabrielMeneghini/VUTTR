package com.apirest.vuttr.services;

import com.apirest.vuttr.dtos.ToolCreateDTO;
import com.apirest.vuttr.dtos.ToolDTO;
import com.apirest.vuttr.dtos.ToolUpdateDTO;
import com.apirest.vuttr.entities.Tool;
import com.apirest.vuttr.exceptions.NoUpdateDetectedException;
import com.apirest.vuttr.helpers.ToolHelper;
import com.apirest.vuttr.repositories.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.apirest.vuttr.helpers.ToolHelper.removeDuplicateAndBlankTags;
import static com.apirest.vuttr.helpers.ToolHelper.updateFieldIfPresent;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;
    private final ToolHelper toolHelper;

    @Transactional(readOnly = true)
    public List<ToolDTO> findTools(int page, int numItems, String tag, String title) {
        return toolRepository.findByTagAndTitle(PageRequest.of(page, numItems), tag, title).stream().map(ToolDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ToolDTO findToolById(Long id) {
        return new ToolDTO(toolHelper.validateToolExists(id));
    }

    @Transactional
    public ToolDTO addTool(ToolCreateDTO dto) {
        var tool = new Tool(dto);
        removeDuplicateAndBlankTags(tool);

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