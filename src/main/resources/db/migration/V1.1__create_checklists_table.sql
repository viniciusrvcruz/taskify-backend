CREATE TABLE checklists (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    is_checked BOOLEAN NOT NULL,
    checked_at TIMESTAMP,
    task_id BIGINT,
    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);