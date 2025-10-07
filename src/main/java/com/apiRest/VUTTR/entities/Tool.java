package com.apiRest.VUTTR.entities;

import com.apiRest.VUTTR.dtos.ToolCreateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tools")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Tool {

    // Fields ----------------------------------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String link;

    private String description;

    @ElementCollection
    @CollectionTable(
            name = "tool_tags",
            joinColumns = @JoinColumn(name = "tool_id")
    )
    @Column(name = "tag")
    private List<String> tags;

    // Constructors ----------------------------------------------------------------------------------------------------
    public Tool(ToolCreateDTO dto) {
        this.title = dto.title();
        this.link = dto.link();
        this.description = dto.description();
        this.tags = dto.tags();
    }

}
