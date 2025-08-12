package com.github.not.n0w.livepilot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(name="name")
    private String name;

    @Column(name="gender")
    private String gender;

    @Column
    @Enumerated(EnumType.STRING)
    private AiTaskType task = AiTaskType.ACQUAINTANCE;

    @Column(name = "usual_dialog_style")
    @Enumerated(EnumType.STRING)
    private DialogStyle usualDialogStyle = DialogStyle.BASE;

    @Transient
    private int extraState = 0; // 0 - regular; 1 - just pushed; 2 - pushed twice and more
}
