CREATE TABLE entreprises (
                             id BIGSERIAL PRIMARY KEY,

                             nom_entreprise VARCHAR(150) NOT NULL,

                             adresse VARCHAR(255),

                             telephone VARCHAR(20),

                             description TEXT,

                             user_id BIGINT UNIQUE NOT NULL,

                             CONSTRAINT fk_entreprise_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES users(id)
                                     ON DELETE CASCADE
);