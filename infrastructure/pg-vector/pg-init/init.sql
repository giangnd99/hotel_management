-- Enable extensions (must match image pgvector >= 0.5.0)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS vector;

-- Optional: switch to schema
-- CREATE SCHEMA IF NOT EXISTS ai_management;
-- SET search_path TO ai_management;

-- Create vector_store table in public schema
-- CREATE TABLE IF NOT EXISTS public.vector_store (
--     id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
--     content TEXT,
--     metadata JSON,
--     embedding vector(768)
--     );
--
-- -- Create HNSW index on vector field
-- CREATE INDEX IF NOT EXISTS spring_ai_vector_index
--     ON public.vector_store USING hnsw (embedding vector_cosine_ops);
