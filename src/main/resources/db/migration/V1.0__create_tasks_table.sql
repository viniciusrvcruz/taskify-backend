CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(200)
);