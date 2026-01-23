package com.apiRest.VUTTR.repositories;

import com.apiRest.VUTTR.entities.Tool;
import org.springframework.data.domain.Page;
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

    @Query("""
        SELECT DISTINCT t
        FROM Tool t
        LEFT JOIN FETCH t.tags
        ORDER BY t.title
    """)
    Page<Tool> findAllFetchingTags(Pageable pageable);

    @Query("""
          SELECT DISTINCT t
          FROM Tool t
          LEFT JOIN FETCH t.tags
          WHERE EXISTS (
              SELECT 1
              FROM t.tags tag
              WHERE LOWER(tag) = LOWER(:tag)
          )
          ORDER BY t.title
    """)
    List<Tool> findByTag(Pageable pageable, @Param("tag") String tag);

    @EntityGraph(attributePaths = "tags")
    Optional<Tool> findById(Long id);

    boolean existsByTitleIgnoreCase(String value);

    boolean existsByLink(String link);

}
