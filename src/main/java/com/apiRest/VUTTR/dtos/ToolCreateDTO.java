package com.apiRest.VUTTR.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ToolCreateDTO(@NotBlank
                            String title,

                            @NotBlank
                            String link,

                            @NotBlank
                            String description,

                            @NotEmpty
                            List<String> tags
) {
}
