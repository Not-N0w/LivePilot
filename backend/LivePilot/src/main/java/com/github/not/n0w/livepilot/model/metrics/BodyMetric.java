package com.github.not.n0w.livepilot.model.metrics;

import com.github.not.n0w.livepilot.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "body_metrics")
public class BodyMetric extends AbstractMetric {}