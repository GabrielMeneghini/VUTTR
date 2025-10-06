-- Inserindo ferramentas
INSERT INTO tools (title, link, description)
VALUES
('Notion', 'https://notion.so', 'All-in-one workspace to write, plan, collaborate, and get organized.'),
('json-server', 'https://github.com/typicode/json-server', 'Fake REST API for prototyping and front-end testing.'),
('fastify', 'https://www.fastify.io/', 'Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.'),
('React', 'https://react.dev', 'JavaScript library for building user interfaces.'),
('Postman', 'https://www.postman.com', 'Collaboration platform for API development and testing.'),
('Docker', 'https://www.docker.com', 'Platform to build, ship, and run applications in containers.');

-- Inserindo tags relacionadas
INSERT INTO tool_tags (tool_id, tag)
VALUES
-- Notion
(1, 'organization'),
(1, 'productivity'),
(1, 'collaboration'),

-- json-server
(2, 'api'),
(2, 'mock'),
(2, 'testing'),

-- fastify (3º elemento)
(3, 'web'),
(3, 'framework'),
(3, 'node'),
(3, 'http2'),
(3, 'https'),
(3, 'localhost'),

-- React
(4, 'frontend'),
(4, 'javascript'),
(4, 'ui'),

-- Postman
(5, 'api'),
(5, 'testing'),
(5, 'tools'),

-- Docker
(6, 'devops'),
(6, 'containers'),
(6, 'infrastructure');
