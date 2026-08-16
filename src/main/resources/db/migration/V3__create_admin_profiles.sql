CREATE TABLE admin_profiles (
                                id BIGSERIAL PRIMARY KEY,
                                nom VARCHAR(255),
                                prenom VARCHAR(255),
                                user_id BIGINT NOT NULL UNIQUE,

                                CONSTRAINT fk_admin_profiles_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
);