package com.apiRest.VUTTR.helpers;

import com.apiRest.VUTTR.entities.Tool;
import com.apiRest.VUTTR.exceptions.ResourceNotFoundException;
import com.apiRest.VUTTR.repositories.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToolHelper {

    private final ToolRepository toolRepository;

    public Tool validateToolExists(Long id) {
        return toolRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No tool was found for id " + id + "."));
    }

    public static void removeDuplicateAndBlankTags(Tool tool) {
        tool.setTags(new ArrayList<>(tool.getTags()
                .stream()
                .filter(tag -> tag!=null && !tag.isBlank())
                .map(tag -> tag.toLowerCase().trim())
                .collect(Collectors.toCollection(LinkedHashSet::new))));
    }

    public static int updateFieldIfPresent(String value, Consumer<String> setter) {
        if(value != null && !value.isBlank()) {
            setter.accept(value.trim());
            return 1;
        }
        return 0;
    }

}
