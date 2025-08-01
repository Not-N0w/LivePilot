package com.github.not.n0w.livepilot.repository;

import com.github.not.n0w.livepilot.model.Chat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Transactional
public interface ChatRepository extends JpaRepository<Chat, String> {

    @Query("SELECT c FROM Chat c LEFT JOIN FETCH c.messages WHERE c.id = :id")
    Optional<Chat> findByIdWithMessages(@Param("id") String id);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO saved_messages (chat_id, message, role) VALUES (?, ?, ?)")
    void saveMessage(String chatId, String message, String role);

}