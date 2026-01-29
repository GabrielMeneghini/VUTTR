package com.apirest.vuttr.dtos;

import com.apirest.vuttr.entities.Tool;

import java.util.List;

public record ToolDTO(Long id,
                      String title,
                      String link,
                      String description,
                      List<String> tags
) {

    public ToolDTO(Tool t) {
        this(
                t.getId(),
                t.getTitle(),
                t.getLink(),
                t.getDescription(),
                t.getTags()
        );
    }

}
