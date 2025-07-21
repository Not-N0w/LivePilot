package com.github.not.n0w.livepilot.model.metrics;

import com.github.not.n0w.livepilot.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@MappedSuperclass
public abstract class AbstractMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Column(name = "created_on", nullable = false)
    protected LocalDate createdOn = LocalDate.now();

    @Column(name = "metric_value", nullable = false)
    protected Integer metricValue;
}