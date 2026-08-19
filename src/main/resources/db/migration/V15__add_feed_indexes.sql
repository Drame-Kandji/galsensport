CREATE INDEX IF NOT EXISTS idx_posts_created_at_desc ON posts(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_posts_author_created_at ON posts(auteur_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_comments_post_created_at ON comments(post_id, created_at DESC);
