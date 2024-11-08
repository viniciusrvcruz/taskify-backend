package br.com.viniciuscruz.taskify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "checklists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 200, nullable = true)
    private String description;

    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked;

    @Column(name = "checked_at", nullable = true)
    private Date checkedAt;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskModel task;
}
