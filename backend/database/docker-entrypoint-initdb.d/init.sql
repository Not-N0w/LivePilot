-- Типы поведения ИИ-агента в диалоге с пользователем
CREATE TYPE dialog_style AS ENUM (
    'base',         -- Нейтральный, сбалансированный стиль (по умолчанию)
    'casual_friend',-- Ненавязчивый друг: дружелюбный, поддерживающий, непринуждённый
    'hyper_helper', -- Гиперактивный помощник: энергичный, инициативный, немного навязчивый
    'dry_expert',   -- Сухой эксперт: формальный, краткий, безэмоциональный
    'coach',        -- Наставник: задаёт вопросы, помогает найти ответ самостоятельно
    'joker',        -- Шутник: встраивает юмор, сарказм или мемы в диалог
    'therapist',    -- Эмпатичный собеседник: поддерживает, выслушивает, помогает справиться с эмоциями
    'interrogator'  -- Допрашивающий: задаёт наводящие, уточняющие, иногда провокационные вопросы
);


CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30),
    gender CHAR(1) CHECK (gender IN ('М', 'Ж')),
    usual_dialog_style dialog_style DEFAULT 'base'
);


CREATE TABLE body_metrics (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    created_on DATE DEFAULT NOW(),
    metric_value INTEGER CHECK (metric_value >= 0 AND metric_value <= 100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE society_metrics (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    created_on DATE DEFAULT NOW(),
    metric_value INTEGER CHECK (metric_value >= 0 AND metric_value <= 100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE mind_metrics (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    created_on DATE DEFAULT NOW(),
    metric_value INTEGER CHECK (metric_value >= 0 AND metric_value <= 100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE growth_metrics (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    created_on DATE DEFAULT NOW(),
    metric_value INTEGER CHECK (metric_value >= 0 AND metric_value <= 100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);





CREATE TABLE chats (
   id VARCHAR(50) PRIMARY KEY,
   user_id INTEGER,
   FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
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
