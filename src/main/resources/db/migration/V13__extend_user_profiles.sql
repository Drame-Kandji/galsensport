ALTER TABLE user_profiles
    ADD COLUMN bio VARCHAR(2000),
    ADD COLUMN ville VARCHAR(120),
    ADD COLUMN date_naissance DATE,
    ADD COLUMN avatar_url VARCHAR(1000),
    ADD COLUMN cover_url VARCHAR(1000);
