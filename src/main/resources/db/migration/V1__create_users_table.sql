CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,

                       nom VARCHAR(100) NOT NULL,

                       prenom VARCHAR(100) NOT NULL,

                       email VARCHAR(150) UNIQUE,

                       telephone VARCHAR(20) UNIQUE,

                       password VARCHAR(255) NOT NULL,

                       role VARCHAR(50) NOT NULL
);