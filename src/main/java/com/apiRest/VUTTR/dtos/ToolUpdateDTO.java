package com.apiRest.VUTTR.dtos;

import com.apiRest.VUTTR.validations.anotations.UniqueLink;
import com.apiRest.VUTTR.validations.anotations.UniqueTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

public record ToolUpdateDTO(@UniqueTitle
                            @Schema(example = "New Title", description = "Tool title")
                            String title,

                            @URL
                            @UniqueLink
                            @Schema(example = "https://newLink.com", description = "Tool link")
                            String link,

                            @Schema(example = "New description", description = "Tool description")
                            String description) {
}
