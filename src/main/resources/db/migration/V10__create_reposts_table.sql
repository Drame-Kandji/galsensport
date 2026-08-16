CREATE TABLE reposts (
                         id BIGSERIAL PRIMARY KEY,

                         user_id BIGINT NOT NULL,
                         post_id BIGINT NOT NULL,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_reposts_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_reposts_post
                             FOREIGN KEY (post_id)
                                 REFERENCES posts(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT uk_reposts_user_post
                             UNIQUE (user_id, post_id)
);