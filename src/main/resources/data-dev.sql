-- =========================
-- TOOLS TABLE
-- =========================
INSERT INTO tools (title, link, description) VALUES
-- 1
('Notion', 'https://notion.so', 'All in one tool to organize teams and ideas. Write, plan, collaborate, and get organized.'),
-- 2
('json-server', 'https://github.com/typicode/json-server', 'Fake REST API based on a JSON schema. Useful for mocking and creating APIs for front-end devs to consume in coding challenges.'),
-- 3
('fastify', 'https://www.fastify.io/', 'Extremely fast and simple, low-overhead web framework for NodeJS. Supports HTTP2.'),
-- 4
('Insomnia', 'https://insomnia.rest/', 'Powerful REST API client with environment variables, GraphQL support, and automatic code generation.'),
-- 5
('Postman', 'https://www.postman.com/', 'Collaborative platform for API development and testing. Includes collections, mocks, and monitoring.'),
-- 6
('Visual Studio Code', 'https://code.visualstudio.com/', 'Lightweight, fast, and extensible code editor with built-in Git and debugging tools.'),
-- 7
('Docker', 'https://www.docker.com/', 'Container platform to build, run, and ship applications consistently across environments.'),
-- 8
('Slack', 'https://slack.com/', 'Collaboration hub for teams to communicate, organize, and integrate tools in one place.'),
-- 9
('GitHub', 'https://github.com/', 'Hosting for software development and version control using Git.'),
-- 10
('GitLab', 'https://gitlab.com/', 'DevOps platform with built-in CI/CD, code reviews, and issue tracking.'),
-- 11
('Bitbucket', 'https://bitbucket.org/', 'Git-based source code repository hosting with Jira and Trello integration.'),
-- 12
('Trello', 'https://trello.com/', 'Visual collaboration tool for planning and organizing tasks using boards and cards.'),
-- 13
('Asana', 'https://asana.com/', 'Project management tool that helps teams organize, track, and manage work.'),
-- 14
('Jira', 'https://www.atlassian.com/software/jira', 'Issue and project tracking software for agile teams.'),
-- 15
('Figma', 'https://figma.com/', 'Collaborative design tool for interface design, prototyping, and feedback.'),
-- 16
('Adobe XD', 'https://adobe.com/xd', 'UI/UX design and prototyping tool for web and mobile apps.'),
-- 17
('Canva', 'https://canva.com/', 'Online design platform for creating social media graphics and presentations.'),
-- 18
('Miro', 'https://miro.com/', 'Online collaborative whiteboard platform for brainstorming and planning.'),
-- 19
('Zoom', 'https://zoom.us/', 'Video conferencing platform for meetings, webinars, and collaboration.'),
-- 20
('Microsoft Teams', 'https://www.microsoft.com/en/microsoft-teams/group-chat-software', 'Communication and collaboration app for teams.'),
-- 21
('Google Meet', 'https://meet.google.com/', 'Video meeting platform integrated with Google Workspace.'),
-- 22
('Discord', 'https://discord.com/', 'Voice, video, and text chat platform for communities and teams.'),
-- 23
('Heroku', 'https://www.heroku.com/', 'Cloud platform to deploy, manage, and scale modern apps.'),
-- 24
('Netlify', 'https://www.netlify.com/', 'Platform for building, deploying, and hosting static websites and web apps.'),
-- 25
('Vercel', 'https://vercel.com/', 'Frontend cloud platform for developing and deploying Next.js and other apps.'),
-- 26
('AWS', 'https://aws.amazon.com/', 'Amazon Web Services – cloud platform offering computing, storage, and more.'),
-- 27
('Google Cloud', 'https://cloud.google.com/', 'Cloud computing services by Google, including hosting and ML APIs.'),
-- 28
('Azure', 'https://azure.microsoft.com/', 'Microsoft’s cloud computing platform for apps, data, and AI.'),
-- 29
('Kubernetes', 'https://kubernetes.io/', 'Open-source system for automating deployment, scaling, and management of containers.'),
-- 30
('Terraform', 'https://www.terraform.io/', 'Infrastructure as Code tool for managing cloud infrastructure safely and efficiently.'),
-- 31
('Ansible', 'https://www.ansible.com/', 'Automation tool for configuration management and application deployment.'),
-- 32
('Jenkins', 'https://www.jenkins.io/', 'Open-source automation server for CI/CD pipelines.'),
-- 33
('CircleCI', 'https://circleci.com/', 'Continuous integration and delivery platform for modern software teams.'),
-- 34
('Travis CI', 'https://travis-ci.com/', 'Hosted continuous integration service for building and testing software projects.'),
-- 35
('Selenium', 'https://www.selenium.dev/', 'Tool suite for automating web browsers for testing purposes.'),
-- 36
('Cypress', 'https://www.cypress.io/', 'End-to-end testing framework for modern web applications.'),
-- 37
('Playwright', 'https://playwright.dev/', 'Automation framework for testing web apps with multiple browsers.'),
-- 38
('PostgreSQL', 'https://www.postgresql.org/', 'Powerful, open-source object-relational database system.'),
-- 39
('MySQL', 'https://www.mysql.com/', 'Open-source relational database management system developed by Oracle.'),
-- 40
('MongoDB', 'https://www.mongodb.com/', 'NoSQL database designed for scalability and developer productivity.'),
-- 41
('Redis', 'https://redis.io/', 'In-memory data store for caching, message brokering, and databases.'),
-- 42
('Elasticsearch', 'https://www.elastic.co/elasticsearch/', 'Distributed search and analytics engine for all types of data.'),
-- 43
('Grafana', 'https://grafana.com/', 'Analytics and monitoring solution for visualizing metrics and logs.'),
-- 44
('Prometheus', 'https://prometheus.io/', 'Monitoring system and time series database for metrics.'),
-- 45
('Swagger', 'https://swagger.io/', 'API design and documentation tools based on the OpenAPI specification.'),
-- 46
('OpenAPI Generator', 'https://openapi-generator.tech/', 'Generates API clients, servers, and docs from OpenAPI specs.'),
-- 47
('Nginx', 'https://nginx.org/', 'Web server that can also be used as a reverse proxy, load balancer, and HTTP cache.'),
-- 48
('Apache', 'https://httpd.apache.org/', 'Open-source HTTP server for hosting and serving web content.'),
-- 49
('Vite', 'https://vitejs.dev/', 'Next generation frontend build tool that is fast and optimized for modern projects.'),
-- 50
('Webpack', 'https://webpack.js.org/', 'Module bundler for JavaScript applications that transforms and bundles assets.');

-- =========================
-- TOOL_TAGS TABLE
-- =========================
INSERT INTO tool_tags (tool_id, tag) VALUES
-- 1 Notion
(1, 'organization'), (1, 'planning'), (1, 'collaboration'), (1, 'writing'), (1, 'calendar'),
-- 2 JSON Server
(2, 'api'), (2, 'json'), (2, 'schema'), (2, 'node'), (2, 'mock'),
-- 3 Fastify
(3, 'framework'), (3, 'node'), (3, 'performance'), (3, 'http2'),
-- 4 Insomnia
(4, 'api'), (4, 'testing'), (4, 'rest'), (4, 'graphql'),
-- 5 Postman
(5, 'api'), (5, 'automation'), (5, 'mock'), (5, 'testing'),
-- 6 VSCode
(6, 'editor'), (6, 'debugger'), (6, 'extensions'), (6, 'git'),
-- 7 Docker
(7, 'containers'), (7, 'deployment'), (7, 'devops'), (7, 'cloud'),
-- 8 Slack
(8, 'chat'), (8, 'communication'), (8, 'remote'), (8, 'integration'),
-- 9 GitHub
(9, 'git'), (9, 'repository'), (9, 'version-control'), (9, 'open-source'),
-- 10 GitLab
(10, 'ci-cd'), (10, 'devops'), (10, 'repository'), (10, 'git'),
-- 11 Bitbucket
(11, 'repository'), (11, 'git'), (11, 'integration'), (11, 'jira'),
-- 12 Trello
(12, 'tasks'), (12, 'boards'), (12, 'kanban'), (12, 'organization'),
-- 13 Asana
(13, 'tasks'), (13, 'projects'), (13, 'management'), (13, 'team'),
-- 14 Jira
(14, 'agile'), (14, 'scrum'), (14, 'bugs'), (14, 'tracking'),
-- 15 Figma
(15, 'design'), (15, 'ui'), (15, 'collaboration'), (15, 'prototype'),
-- 16 Adobe XD
(16, 'design'), (16, 'ui-ux'), (16, 'prototype'), (16, 'creative'),
-- 17 Canva
(17, 'design'), (17, 'templates'), (17, 'graphics'), (17, 'social-media'),
-- 18 Miro
(18, 'whiteboard'), (18, 'collaboration'), (18, 'brainstorm'), (18, 'remote'),
-- 19 Zoom
(19, 'video'), (19, 'meetings'), (19, 'communication'), (19, 'conference'),
-- 20 Teams
(20, 'chat'), (20, 'meetings'), (20, 'office365'), (20, 'collaboration'),
-- 21 Google Meet
(21, 'video'), (21, 'google'), (21, 'meetings'), (21, 'conference'),
-- 22 Discord
(22, 'voice'), (22, 'chat'), (22, 'community'), (22, 'gaming'),
-- 23 Heroku
(23, 'cloud'), (23, 'deployment'), (23, 'paas'), (23, 'apps'),
-- 24 Netlify
(24, 'hosting'), (24, 'frontend'), (24, 'deploy'), (24, 'cdn'),
-- 25 Vercel
(25, 'nextjs'), (25, 'frontend'), (25, 'deploy'), (25, 'cloud'),
-- 26 AWS
(26, 'cloud'), (26, 'infrastructure'), (26, 'scalable'), (26, 'storage'),
-- 27 Google Cloud
(27, 'cloud'), (27, 'machine-learning'), (27, 'storage'), (27, 'infrastructure'),
-- 28 Azure
(28, 'cloud'), (28, 'microsoft'), (28, 'ai'), (28, 'devops'),
-- 29 Kubernetes
(29, 'containers'), (29, 'orchestration'), (29, 'cluster'), (29, 'automation'),
-- 30 Terraform
(30, 'iac'), (30, 'infrastructure'), (30, 'automation'), (30, 'cloud'),
-- 31 Ansible
(31, 'automation'), (31, 'configuration'), (31, 'deployment'), (31, 'devops'),
-- 32 Jenkins
(32, 'ci-cd'), (32, 'automation'), (32, 'pipeline'), (32, 'build'),
-- 33 CircleCI
(33, 'ci'), (33, 'deployment'), (33, 'automation'), (33, 'testing'),
-- 34 Travis CI
(34, 'ci'), (34, 'testing'), (34, 'github'), (34, 'build'),
-- 35 Selenium
(35, 'testing'), (35, 'automation'), (35, 'browser'), (35, 'qa'),
-- 36 Cypress
(36, 'testing'), (36, 'frontend'), (36, 'e2e'), (36, 'automation'),
-- 37 Playwright
(37, 'testing'), (37, 'browser'), (37, 'e2e'), (37, 'automation'),
-- 38 PostgreSQL
(38, 'database'), (38, 'sql'), (38, 'open-source'), (38, 'relational'),
-- 39 MySQL
(39, 'database'), (39, 'sql'), (39, 'storage'), (39, 'relational'),
-- 40 MongoDB
(40, 'database'), (40, 'nosql'), (40, 'json'), (40, 'scalable'),
-- 41 Redis
(41, 'cache'), (41, 'database'), (41, 'in-memory'), (41, 'queue'),
-- 42 Elasticsearch
(42, 'search'), (42, 'analytics'), (42, 'logs'), (42, 'index'),
-- 43 Grafana
(43, 'dashboard'), (43, 'metrics'), (43, 'monitoring'), (43, 'visualization'),
-- 44 Prometheus
(44, 'monitoring'), (44, 'metrics'), (44, 'alerts'), (44, 'time-series'),
-- 45 Swagger
(45, 'api'), (45, 'documentation'), (45, 'openapi'), (45, 'rest'),
-- 46 OpenAPI Generator
(46, 'api'), (46, 'codegen'), (46, 'openapi'), (46, 'generator'),
-- 47 Nginx
(47, 'server'), (47, 'proxy'), (47, 'load-balancer'), (47, 'http'),
-- 48 Apache
(48, 'server'), (48, 'web'), (48, 'open-source'), (48, 'http'),
-- 49 Vite
(49, 'build'), (49, 'frontend'), (49, 'javascript'), (49, 'fast'),
-- 50 Webpack
(50, 'bundler'), (50, 'javascript'), (50, 'frontend'), (50, 'build');
