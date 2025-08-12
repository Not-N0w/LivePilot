package com.github.not.n0w.lifepilot.repository;

import com.github.not.n0w.lifepilot.model.Chat;
import com.github.not.n0w.lifepilot.model.SavedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SavedMessagesRepository extends JpaRepository<SavedMessage, String> {
    List<SavedMessage> findAllByChatId(String chatId);
}