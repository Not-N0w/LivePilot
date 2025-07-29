package com.github.not.n0w.livepilot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Data
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer metricValue;

    @Enumerated(EnumType.STRING)
    private MetricType metricType;

}
