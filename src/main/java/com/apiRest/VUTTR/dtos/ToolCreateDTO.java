package com.apiRest.VUTTR.dtos;

import java.util.List;

public record ToolCreateDTO(String title,
                            String link,
                            String description,
                            List<String> tags
) {
}
