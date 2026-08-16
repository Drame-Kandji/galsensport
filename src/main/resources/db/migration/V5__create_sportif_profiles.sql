CREATE TABLE sportif_profiles (
                                  id BIGSERIAL PRIMARY KEY,

                                  user_id BIGINT NOT NULL,

                                  sport VARCHAR(100) NOT NULL,

                                  poste VARCHAR(100),

                                  niveau VARCHAR(50),

                                  bio VARCHAR(1000),

                                  ville VARCHAR(100),

                                  CONSTRAINT uk_sportif_profiles_user
                                      UNIQUE (user_id),

                                  CONSTRAINT fk_sportif_profiles_user
                                      FOREIGN KEY (user_id)
                                          REFERENCES users(id)
                                          ON DELETE CASCADE
);