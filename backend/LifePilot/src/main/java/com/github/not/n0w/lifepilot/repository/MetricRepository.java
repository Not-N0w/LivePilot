package com.github.not.n0w.lifepilot.repository;

import com.github.not.n0w.lifepilot.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

    @Query(value = """
        SELECT * FROM metrics m
        WHERE m.chat_id = :chatId AND m.created_at = (
            SELECT MAX(m2.created_at)
            FROM metrics m2
            WHERE m2.chat_id = :chatId
        )
    """, nativeQuery = true)
    List<Metric> findLatestMetricsByChatId(@Param("chatId") String chatId);


}
