package com.github.not.n0w.livepilot.repository;

import com.github.not.n0w.livepilot.model.SavedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SavedMessagesRepository extends JpaRepository<SavedMessage, String> {
    List<SavedMessage> findAllByUserId(Long userId);
}