package com.github.not.n0w.livepilot.model;

import com.github.not.n0w.livepilot.model.metrics.BodyMetric;
import com.github.not.n0w.livepilot.model.metrics.GrowthMetric;
import com.github.not.n0w.livepilot.model.metrics.MindMetric;
import com.github.not.n0w.livepilot.model.metrics.SocietyMetric;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    @Column(length = 1)
    private char gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "usual_dialog_style", nullable = false)
    private DialogStyle usualDialogStyle = DialogStyle.BASE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BodyMetric> bodyMetrics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocietyMetric> societyMetrics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MindMetric> mindMetrics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrowthMetric> growthMetrics;
}