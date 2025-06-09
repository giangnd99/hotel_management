-- init-schema.sql
DROP SCHEMA IF EXISTS ai_management CASCADE;

CREATE SCHEMA ai_management;

-- Drop tables in reverse order of dependency to avoid foreign key constraints issues
DROP TABLE IF EXISTS training_jobs;
DROP TABLE IF EXISTS embeddings;
DROP TABLE IF EXISTS responses;
DROP TABLE IF EXISTS prompts;
DROP TABLE IF EXISTS datasets;
DROP TABLE IF EXISTS ai_models;


-- Create ai_models table
CREATE TABLE ai_models (
                           id VARCHAR(255) PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           provider VARCHAR(255) NOT NULL,
                           version VARCHAR(255) NOT NULL,
                           is_active BOOLEAN NOT NULL
);

-- Create datasets table
CREATE TABLE datasets (
                          id VARCHAR(255) PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          source VARCHAR(255) NOT NULL,
                          size BIGINT NOT NULL
);

-- Create prompts table
CREATE TABLE prompts (
                         id VARCHAR(255) PRIMARY KEY,
                         text TEXT NOT NULL,
                         ai_model_id VARCHAR(255) NOT NULL,
                         CONSTRAINT fk_prompts_ai_model FOREIGN KEY (ai_model_id) REFERENCES ai_models (id)
);

-- Create responses table
CREATE TABLE responses (
                           id VARCHAR(255) PRIMARY KEY,
                           generated_text TEXT NOT NULL,
                           ai_model_id VARCHAR(255) NOT NULL,
                           prompt_id VARCHAR(255) NOT NULL,
                           CONSTRAINT fk_responses_ai_model FOREIGN KEY (ai_model_id) REFERENCES ai_models (id),
                           CONSTRAINT fk_responses_prompt FOREIGN KEY (prompt_id) REFERENCES prompts (id)
);

-- Create embeddings table
CREATE TABLE embeddings (
                            id VARCHAR(255) PRIMARY KEY,
                            vector TEXT NOT NULL,
                            prompt_id VARCHAR(255),
                            model_id VARCHAR(255),
                            CONSTRAINT fk_embeddings_prompt FOREIGN KEY (prompt_id) REFERENCES prompts (id),
                            CONSTRAINT fk_embeddings_ai_model FOREIGN KEY (model_id) REFERENCES ai_models (id)
);

-- Create training_jobs table
CREATE TABLE training_jobs (
                               id VARCHAR(255) PRIMARY KEY,
                               status VARCHAR(255) NOT NULL,
                               model_id VARCHAR(255) NOT NULL,
                               dataset_id VARCHAR(255) NOT NULL,
                               error_messages TEXT[], -- PostgreSQL array type for error messages
                               CONSTRAINT fk_training_jobs_model FOREIGN KEY (model_id) REFERENCES ai_models (id),
                               CONSTRAINT fk_training_jobs_dataset FOREIGN KEY (dataset_id) REFERENCES datasets (id)
);

-- You can add indexes here if needed for performance
-- For example:
-- CREATE INDEX idx_prompts_ai_model_id ON prompts (ai_model_id);
-- CREATE INDEX idx_responses_ai_model_id ON responses (ai_model_id);
-- CREATE INDEX idx_responses_prompt_id ON responses (prompt_id);
-- CREATE INDEX idx_embeddings_prompt_id ON embeddings (prompt_id);
-- CREATE INDEX idx_embeddings_model_id ON embeddings (model_id);
-- CREATE INDEX idx_training_jobs_model_id ON training_jobs (model_id);
-- CREATE INDEX idx_training_jobs_dataset_id ON training_jobs (dataset_id);