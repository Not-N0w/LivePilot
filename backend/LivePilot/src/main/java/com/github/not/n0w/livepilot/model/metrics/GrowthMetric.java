package com.github.not.n0w.livepilot.model.metrics;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "growth_metrics")
public class GrowthMetric extends AbstractMetric {}
