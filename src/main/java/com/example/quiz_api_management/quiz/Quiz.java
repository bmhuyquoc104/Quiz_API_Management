package com.example.quiz_api_management.quiz;

import com.example.quiz_api_management.question.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz")
@Getter
@Setter
public class Quiz {
    @Id
    @SequenceGenerator(
            name = "quiz_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "quiz_sequence"
    )

    @Column(name = "id", nullable = false)
    private int id;
    @Column(name="value", nullable = false)
    private String value;

    @Column(name="type", nullable = false)
    private String type;

    // A quiz has a list of questions with one-to-many relationship
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Question> questions;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Quiz(String value, String type) {
        this.value = value;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Quiz() {
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", questions=" + questions +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
