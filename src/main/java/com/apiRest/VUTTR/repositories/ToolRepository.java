package com.apiRest.VUTTR.repositories;

import com.apiRest.VUTTR.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToolRepository extends JpaRepository<Tool, Long> {

    @Query(nativeQuery = true, value = """
            SELECT t.*
            FROM tools t
            INNER JOIN tool_tags tt
                ON t.id = tt.tool_id
            WHERE tt.tag LIKE :tag
            """)
    List<Tool> findByTag(@Param("tag") String tag);

}
