package com.apiRest.VUTTR.dtos;

import com.apiRest.VUTTR.validations.anotations.UniqueLink;
import com.apiRest.VUTTR.validations.anotations.UniqueTitle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ToolCreateDTO(@NotBlank
                            @UniqueTitle
                            String title,

                            @NotBlank
                            @URL
                            @UniqueLink
                            String link,

                            @NotBlank
                            String description,

                            @NotEmpty
                            List<String> tags
) {
}
