CREATE TABLE follows (
                         id BIGSERIAL PRIMARY KEY,

                         follower_id BIGINT NOT NULL,
                         following_id BIGINT NOT NULL,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_follows_follower
                             FOREIGN KEY (follower_id)
                                 REFERENCES users(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_follows_following
                             FOREIGN KEY (following_id)
                                 REFERENCES users(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT uk_follows_follower_following
                             UNIQUE (follower_id, following_id),

                         CONSTRAINT chk_follows_not_self
                             CHECK (follower_id <> following_id)
);