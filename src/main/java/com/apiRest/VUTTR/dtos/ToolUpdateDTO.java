package com.apiRest.VUTTR.dtos;

import com.apiRest.VUTTR.validations.anotations.UniqueLink;
import com.apiRest.VUTTR.validations.anotations.UniqueTitle;
import org.hibernate.validator.constraints.URL;

public record ToolUpdateDTO(@UniqueTitle
                            String title,

                            @URL
                            @UniqueLink
                            String link,

                            String description) {
}
