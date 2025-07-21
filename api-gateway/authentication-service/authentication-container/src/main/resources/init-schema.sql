DROP SCHEMA IF EXISTS authentication_service CASCADE;

CREATE SCHEMA authentication_service;

-- Table: roles
CREATE TABLE roles (
                       id VARCHAR(255) PRIMARY KEY,
                       name VARCHAR(255)
);

-- Table: users
CREATE TABLE users (
                       user_id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(255),
                       role_id VARCHAR(255),
                       CONSTRAINT fk_role
                           FOREIGN KEY(role_id)
                               REFERENCES roles(id)
);

-- Table: token_entity
-- Assuming token_entity is the table name for TokenEntity
CREATE TABLE token_entity (
                              id VARCHAR(255) PRIMARY KEY,
                              expiry_time TIMESTAMP
);