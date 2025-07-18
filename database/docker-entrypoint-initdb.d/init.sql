CREATE TABLE chats (
   id VARCHAR(50) PRIMARY KEY
);

CREATE TABLE saved_messages (
    id SERIAL PRIMARY KEY,
    chat_id VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE
);


CREATE OR REPLACE FUNCTION chat_history_length_check()
    RETURNS TRIGGER AS $$
DECLARE
    max_history_length INTEGER;
    message_count INTEGER;
    excess INTEGER;
BEGIN
    BEGIN
        max_history_length := current_setting('ai.history.length')::INTEGER;
    EXCEPTION
        WHEN others THEN
            max_history_length := 10;
    END;

    SELECT COUNT(*) INTO message_count
    FROM saved_messages
    WHERE chat_id = NEW.chat_id;

    IF message_count >= max_history_length THEN
        excess := message_count - max_history_length + 1;

        DELETE FROM saved_messages
        WHERE id IN (
            SELECT id FROM saved_messages
            WHERE chat_id = NEW.chat_id
            ORDER BY id ASC
            LIMIT excess
        );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_check_chat_history_length
    BEFORE INSERT ON saved_messages
    FOR EACH ROW
EXECUTE FUNCTION chat_history_length_check();
