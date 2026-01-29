package com.apirest.vuttr.dtos;

import com.apirest.vuttr.validations.anotations.UniqueLink;
import com.apirest.vuttr.validations.anotations.UniqueTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ToolCreateDTO(@NotBlank
                            @UniqueTitle
                            @Schema(example = "Tool title")
                            String title,

                            @NotBlank
                            @URL
                            @UniqueLink
                            @Schema(example = "https://ToolLink.com", description = "Tool link")
                            String link,

                            @NotBlank
                            @Schema(example = "Tool description")
                            String description,

                            @NotEmpty
                            @Schema(
                                    example = "[\"java\", \"spring\", \"api\"]",
                                    description = "List of tags associated with the tool"
                            )
                            List<String> tags) {
}
