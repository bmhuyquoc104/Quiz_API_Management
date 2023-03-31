package com.example.quiz_api_management.answer;

import com.example.quiz_api_management.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table (name = "answer")
/*
 These annotations belong to lombok, which can generate setter and getter methods automatically
 Moreover, it reduces verbosity of the code and avoid repetition.
 */
@Getter
@Setter
public class Answer {
    @Id
    /*
      Annotation SequenceGenerator is provoked for defining primary keys.
      allocationSize is used for how many sequences in memory are allocated to the database
     */
    @SequenceGenerator(
            name = "answer_sequence",
            sequenceName = "answer_sequence",
            allocationSize = 1
    )

    /*
      Annotation GeneratedValue is provoked for generating value of a primary key
     */
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "answer_sequence"
    )

    @Column(name="id", unique = true)
    private int id;

    @Column(name="value", nullable = false)
    private String value;

    @Column(name="is_correct", nullable = false)
    private boolean isCorrect;

    @Transient
    @Column(name="is_removed", nullable = false)
    private boolean isRemoved;
    @ManyToOne
    @JoinColumn(name = "question_id")
    /*
    JsonManageReference used to avoid infinite recursion
     */
    // @JsonManagedReference(value = "id") - If it exists, it simply cannot create Answer.
    private Question question;

    /*
    LocalDate provides only Date but costs memory than LocalDateTime,
    while LocalDateTime provides full information about time (hour, minute, second and millisecond).
    Currently, there is no need for full information of time in Answer entity (like sorting).
     */
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    public Answer(String value, boolean isCorrect, Question question) {
        this.value = value;
        this.isCorrect = isCorrect;
        this.isRemoved = false;
        this.question = question;
        this.createdAt = LocalDate.now(); // We don't need to sort or filter so LocalDate is enough because of memory
        this.updatedAt = LocalDate.now();
    }

    public Answer() {
    }

    public Answer(AnswerDTO reqBody, Question question) {
        this.value = reqBody.getValue();
        this.isCorrect = reqBody.isCorrect();
        this.question = question;
    }
}
