CREATE TABLE posts (
                       id BIGSERIAL PRIMARY KEY,
                       contenu TEXT,
                       auteur_id BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,

                       CONSTRAINT fk_posts_auteur
                           FOREIGN KEY (auteur_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE
);