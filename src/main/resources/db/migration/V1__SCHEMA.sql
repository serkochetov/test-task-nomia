-- Enable LTree
CREATE EXTENSION IF NOT EXISTS LTREE WITH SCHEMA public;

-- Create table
CREATE TABLE IF NOT EXISTS section (
  id serial PRIMARY KEY,
  name text,
  path ltree
);

CREATE TABLE IF NOT EXISTS product (
  id serial PRIMARY KEY,
  name text,
  section_id bigint
    constraint section_id
    references section
);

-- Create index
CREATE INDEX IF NOT EXISTS section_path_idx ON section USING gist (path);