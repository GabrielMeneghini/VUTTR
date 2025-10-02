CREATE TABLE tools (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    link VARCHAR(255),
    description TEXT
);

CREATE TABLE tool_tags (
    tool_id BIGINT NOT NULL,
    tag VARCHAR(255) NOT NULL,
    CONSTRAINT fk_tool FOREIGN KEY (tool_id) REFERENCES tools(id) ON DELETE CASCADE
);