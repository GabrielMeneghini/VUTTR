package com.apiRest.VUTTR.dtos;

import com.apiRest.VUTTR.validations.anotations.UniqueLink;
import com.apiRest.VUTTR.validations.anotations.UniqueTitle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ToolCreateDTO(@NotBlank
                            @UniqueTitle
                            String title,

                            @NotBlank
                            @UniqueLink
                            String link,

                            @NotBlank
                            String description,

                            @NotEmpty
                            List<String> tags
) {
}
