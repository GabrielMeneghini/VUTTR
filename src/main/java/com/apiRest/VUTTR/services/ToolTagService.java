package com.apiRest.VUTTR.services;

import com.apiRest.VUTTR.dtos.ToolDTO;
import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.helpers.ToolHelper;
import com.apiRest.VUTTR.repositories.ToolRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.apiRest.VUTTR.helpers.ToolHelper.removeDuplicateTags;

@Service
public class ToolTagService {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ToolHelper toolHelper;

    @Transactional
    public ToolDTO addTagsInTool(List<String> newTags, Long id) {
        Tool tool = toolHelper.validateToolExists(id);

        tool.getTags().addAll(newTags);
        removeDuplicateTags(tool);

        return new ToolDTO(tool);
    }

    @Transactional
    public void deleteToolTagByName(Long toolId, @NotEmpty List<String> toBeDeletedTags) {
        var tool = toolHelper.validateToolExists(toolId);

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
    public ToolDTO updateAllToolTags(Long toolId, List<String> allNewTags) {
        var tool = toolHelper.validateToolExists(toolId);

        tool.setTags(allNewTags);
        removeDuplicateTags(tool);

        return new ToolDTO(tool);
    }

}
