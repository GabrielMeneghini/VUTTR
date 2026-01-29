package com.apirest.vuttr.testhelpers;

import com.apirest.vuttr.entities.Tool;
import com.apirest.vuttr.repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ToolTestHelper {

    @Autowired
    private ToolRepository toolRepository;

    public List<Tool> createAndSaveTools() {
        Tool tool1 = new Tool(
                null,
                "Notion",
                "https://notion.so",
                "All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.",
                Arrays.asList("organization", "planning", "collaboration", "existentTag", "writing", "calendar", "web")
        );

        Tool tool2 = new Tool(
                null,
                "json-server",
                "https://github.com/typicode/json-server",
                "Fake REST API based on a json schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.",
                Arrays.asList("api", "json", "schema", "node", "github", "rest")
        );

        Tool tool3 = new Tool(
                null,
                "fastify",
                "https://www.fastify.io/",
                "Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.",
                Arrays.asList("existentTag", "web", "framework", "node", "http2", "https", "localhost")
        );

        Tool tool4 = new Tool(
                null,
                "Docker",
                "https://www.docker.com/",
                "Container platform to build, run, and ship applications consistently across environments.",
                Arrays.asList("containers", "devops", "deployment", "cloud")
        );

        Tool tool5 = new Tool(
                null,
                "Postman",
                "https://www.postman.com/",
                "Collaborative platform for API development and testing, including collections and mocks.",
                Arrays.asList("api", "testing", "rest", "web", "automation")
        );

        Tool tool6 = new Tool(
                null,
                "GitHub",
                "https://github.com/",
                "Platform for version control and collaborative software development using Git.",
                Arrays.asList("git", "repository", "version-control", "open-source")
        );

        Tool tool7 = new Tool(
                null,
                "Visual Studio Code",
                "https://code.visualstudio.com/",
                "Lightweight and powerful source code editor with debugging and Git integration.",
                Arrays.asList("editor", "debugger", "extensions", "git")
        );

        Tool tool8 = new Tool(
                null,
                "PostgreSQL",
                "https://www.postgresql.org/",
                "Advanced open-source relational database with strong consistency and extensibility.",
                Arrays.asList("database", "sql", "relational", "open-source")
        );

        Tool tool9 = new Tool(
                null,
                "Kubernetes",
                "https://kubernetes.io/",
                "System for automating deployment, scaling, and management of containerized applications.",
                Arrays.asList("web", "containers", "orchestration", "devops", "cloud")
        );

        Tool tool10 = new Tool(
                null,
                "MySQL",
                "https://www.mysql.com/",
                "Open-source relational database management system widely used for web applications.",
                Arrays.asList("database", "sql", "relational", "storage", "mysql")
        );

        return toolRepository.saveAll(Arrays.asList(tool1, tool2, tool3, tool4, tool5, tool6, tool7, tool8, tool9, tool10));
    }

}