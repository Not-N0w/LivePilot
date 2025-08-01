package com.github.not.n0w.livepilot.repository;

import com.github.not.n0w.livepilot.model.Chat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


@Transactional
public interface ChatRepository extends JpaRepository<Chat, String> {

    @Transactional
    @Modifying
    @Query("UPDATE Chat c SET c.task = 'GET_METRICS'")
    int globalSetTaskGetMetrics();
}