package com.github.not.n0w.livepilot.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @Column(name = "id")
    private String id;

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
    private int extraState = 0;
}
