-- Inserting data into the tools table
INSERT INTO tools (title, link, description)
VALUES
('Notion', 'https://notion.so', 'All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.'),
('json-server', 'https://github.com/typicode/json-server', 'Fake REST API based on a JSON schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.'),
('fastify', 'https://www.fastify.io/', 'Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.'),
('Insomnia', 'https://insomnia.rest/', 'Powerful REST API client with environment variables, GraphQL support, and automatic code generation.'),
('Postman', 'https://www.postman.com/', 'Collaborative platform for API development and testing. Includes collections, mocks, and monitoring.'),
('Visual Studio Code', 'https://code.visualstudio.com/', 'Lightweight, fast, and extensible code editor with built-in Git and debugging tools.'),
('Docker', 'https://www.docker.com/', 'Container platform to build, run, and ship applications consistently across environments.'),
('Slack', 'https://slack.com/', 'Collaboration hub for teams to communicate, organize, and integrate tools in one place.');

-- Inserting tags for Notion
INSERT INTO tool_tags (tool_id, tag) VALUES
(1, 'organization'),
(1, 'planning'),
(1, 'collaboration'),
(1, 'writing'),
(1, 'calendar');

-- Inserting tags for JSON Server
INSERT INTO tool_tags (tool_id, tag) VALUES
(2, 'api'),
(2, 'json'),
(2, 'schema'),
(2, 'node'),
(2, 'github'),
(2, 'rest');

-- Inserting tags for Fastify
INSERT INTO tool_tags (tool_id, tag) VALUES
(3, 'web'),
(3, 'framework'),
(3, 'node'),
(3, 'http2'),
(3, 'https'),
(3, 'localhost');

-- Inserting tags for Insomnia
INSERT INTO tool_tags (tool_id, tag) VALUES
(4, 'api'),
(4, 'testing'),
(4, 'rest'),
(4, 'graphql'),
(4, 'client');

-- Inserting tags for Postman
INSERT INTO tool_tags (tool_id, tag) VALUES
(5, 'api'),
(5, 'testing'),
(5, 'collections'),
(5, 'mock'),
(5, 'automation');

-- Inserting tags for Visual Studio Code
INSERT INTO tool_tags (tool_id, tag) VALUES
(6, 'editor'),
(6, 'code'),
(6, 'debugger'),
(6, 'git'),
(6, 'extensions');

-- Inserting tags for Docker
INSERT INTO tool_tags (tool_id, tag) VALUES
(7, 'containers'),
(7, 'devops'),
(7, 'infrastructure'),
(7, 'images'),
(7, 'deployment');

-- Inserting tags for Slack
INSERT INTO tool_tags (tool_id, tag) VALUES
(8, 'communication'),
(8, 'team'),
(8, 'integration'),
(8, 'chat'),
(8, 'remote');
