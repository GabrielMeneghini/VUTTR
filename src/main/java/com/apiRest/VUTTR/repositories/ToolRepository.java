package com.apiRest.VUTTR.repositories;

import com.apiRest.VUTTR.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, Long> {
}
