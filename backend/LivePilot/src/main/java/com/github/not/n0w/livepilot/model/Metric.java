package com.github.not.n0w.livepilot.model;

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

    private LocalDate createdOn = LocalDate.now();

    private Integer metricValue;

    @Enumerated(EnumType.STRING)
    private MetricType metricType;

}
