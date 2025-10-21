package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.dtos.ToolUpdateDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.exceptions.ResourceNotFoundException;
import com.apiRest.VUTTR.repositories.ToolRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        var tool = new Tool(dto);
        removeDuplicateTags(tool);

        return new ToolDTO(toolRepository.save(tool));
    }

    @Transactional
    public ToolDTO addTagsInTool(List<String> newTags, Long id) {
        Tool tool = validateToolExists(id);

        tool.getTags().addAll(newTags);
        removeDuplicateTags(tool);

        return new ToolDTO(tool);
    }

    @Transactional
    public void deleteToolById(Long id) {
        Tool tool = validateToolExists(id);

        toolRepository.deleteById(tool.getId());
    }

    @Transactional
    public void deleteToolTagByName(Long toolId, @NotEmpty List<String> toBeDeletedTags) {
        var tool = validateToolExists(toolId);

        var lowerCaseTags = toBeDeletedTags.stream().map(String::toLowerCase).toList();

        // Iterates over lowerCaseTags and removes matching tags from the tool's tag list
        for(int x=0; x<lowerCaseTags.size(); x++) {
            for(int y=0; y<tool.getTags().size(); y++) {
                if(lowerCaseTags.get(x).equals(tool.getTags().get(y))) {
                    tool.getTags().remove(y);
                    break;
                }
            }
        }
    }

    @Transactional
    public ToolDTO updateTool(ToolUpdateDTO toolUpdateDTO, Long id) {
        Tool tool = validateToolExists(id);

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

    @Transactional
    public ToolDTO updateAllToolTags(Long toolId, List<String> allNewTags) {
        var tool = validateToolExists(toolId);

        tool.setTags(allNewTags);
        removeDuplicateTags(tool);

        return new ToolDTO(tool);
    }

    private Tool validateToolExists(Long id) {
        return toolRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No tool was found for id " + id + "."));
    }

    private void removeDuplicateTags(Tool tool) {
        tool.setTags(new ArrayList<>(tool.getTags()
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedHashSet::new))));
    }
}
