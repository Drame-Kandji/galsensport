ALTER TABLE comments
    ADD COLUMN parent_id BIGINT NULL;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_parent
        FOREIGN KEY (parent_id)
        REFERENCES comments(id)
        ON DELETE CASCADE;

CREATE INDEX idx_comments_post_parent_created_at
    ON comments(post_id, parent_id, created_at DESC);
