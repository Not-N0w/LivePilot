package com.github.not.n0w.livepilot.model;

import com.github.not.n0w.livepilot.aiAgent.task.tasks.getMetrics.MetricType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "metrics")
@Data
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;

    @Enumerated(EnumType.STRING)
    private MetricType metricType;

    private LocalDate createdOn = LocalDate.now();

    private Integer metricValue;

}
