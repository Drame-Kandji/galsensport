CREATE TABLE conversations (id BIGSERIAL PRIMARY KEY, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);
CREATE TABLE conversation_participants (conversation_id BIGINT NOT NULL REFERENCES conversations(id) ON DELETE CASCADE, user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE, PRIMARY KEY(conversation_id,user_id));
CREATE TABLE messages (id BIGSERIAL PRIMARY KEY, conversation_id BIGINT NOT NULL REFERENCES conversations(id) ON DELETE CASCADE, sender_id BIGINT NOT NULL REFERENCES users(id), content TEXT NOT NULL, read_at TIMESTAMP, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);
CREATE INDEX idx_messages_conversation_created ON messages(conversation_id,created_at DESC);
