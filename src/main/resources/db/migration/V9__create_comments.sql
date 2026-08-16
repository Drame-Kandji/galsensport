CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,

                          contenu TEXT NOT NULL,

                          auteur_id BIGINT NOT NULL,

                          post_id BIGINT NOT NULL,

                          created_at TIMESTAMP NOT NULL,

                          updated_at TIMESTAMP NOT NULL,

                          CONSTRAINT fk_comments_auteur
                              FOREIGN KEY (auteur_id)
                                  REFERENCES users(id),

                          CONSTRAINT fk_comments_post
                              FOREIGN KEY (post_id)
                                  REFERENCES posts(id)
);