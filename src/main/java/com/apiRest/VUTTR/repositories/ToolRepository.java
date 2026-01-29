package com.apirest.vuttr.repositories;

import com.apirest.vuttr.entities.Tool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    @EntityGraph(attributePaths = "tags")
    @Query("""
        SELECT DISTINCT t
        FROM Tool t
        WHERE
          (:tag IS NULL OR EXISTS (
              SELECT 1
              FROM t.tags tag
              WHERE LOWER(tag) = :tag
          ))
        AND
          (:title IS NULL OR LOWER(t.title) LIKE CONCAT('%', CAST(:title AS string), '%'))
        ORDER BY t.title
    """)
    Page<Tool> findByTagAndTitle(Pageable pageable, @Param("tag") String tag, @Param("title") String title);

    @EntityGraph(attributePaths = "tags")
    Optional<Tool> findById(Long id);

    boolean existsByTitleIgnoreCase(String value);

    boolean existsByLink(String link);
}
