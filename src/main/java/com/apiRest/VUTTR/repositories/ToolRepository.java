package com.apiRest.VUTTR.repositories;

import com.apiRest.VUTTR.entities.Tool;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    @Query(nativeQuery = true, value = """
            SELECT t.*
            FROM tools t
            INNER JOIN tool_tags tt
                ON t.id = tt.tool_id
            WHERE tt.tag ILIKE :tag
            """)
    List<Tool> findByTag(Pageable pageable, @Param("tag") String tag);

    @EntityGraph(attributePaths = "tags")
    Optional<Tool> findById(Long id);

    boolean existsByTitleIgnoreCase(String value);

    boolean existsByLink(String link);
}
