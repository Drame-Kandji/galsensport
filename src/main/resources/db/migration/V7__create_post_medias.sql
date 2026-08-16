CREATE TABLE post_medias (
                             id BIGSERIAL PRIMARY KEY,
                             post_id BIGINT NOT NULL,
                             type VARCHAR(20) NOT NULL,
                             url VARCHAR(1000) NOT NULL,
                             ordre INTEGER NOT NULL,

                             CONSTRAINT fk_post_medias_post
                                 FOREIGN KEY (post_id)
                                     REFERENCES posts(id)
                                     ON DELETE CASCADE
);