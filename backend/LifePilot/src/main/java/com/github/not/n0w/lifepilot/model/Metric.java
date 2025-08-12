package com.github.not.n0w.lifepilot.model;

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

    private Long userId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer metricValue;

    @Enumerated(EnumType.STRING)
    private MetricType metricType;

    @Override
    public String toString() {
        return metricType + ": " + metricValue;
    }
}
