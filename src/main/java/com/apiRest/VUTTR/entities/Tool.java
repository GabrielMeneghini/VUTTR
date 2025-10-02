package com.apiRest.VUTTR.entities;

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

}
