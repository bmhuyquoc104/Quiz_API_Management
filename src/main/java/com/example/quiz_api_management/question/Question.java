package com.example.quiz_api_management.question;

import com.example.quiz_api_management.answer.Answer;
import com.example.quiz_api_management.quiz.Quiz;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name="question")
@Getter
@Setter
public class Question {
    @Id
    @SequenceGenerator(
            name = "question_sequence",
            sequenceName = "question_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "question_sequence"
    )
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "value", nullable = false)
    private String value;
    @Column(name = "type", nullable = false)
    @NotNull
    private String type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /*
    Cascade in Hibernate is the way to achieve dependent relationship (in this case, this relationship is aggregration).
    In this case we have relationship Question-Answer, if question is deleted completely, list of answers also are deleted.
    Without Question, the Answer model does not make sense if it exists alone
    */

    /*
    Updated: JsonBackReference used to avoid infinite recursion.
    Also, it is used to not show list of answers when serializing
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Answer> answers;

    /*
    While question-quiz has a many-to-one relationship,
    quiz-question only has a zero-to-one relationship
     */
    @ManyToOne()
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public Question(String value, String type, Quiz quiz) {
        this.value = value;
        this.type = type;
        this.quiz = quiz;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Question() {

    }
}
