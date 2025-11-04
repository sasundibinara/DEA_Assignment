package com.alpms.al_paper_management.exams.model;

import com.alpms.al_paper_management.subjects.model.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "exam_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "2025 A/L Physics Paper Discussion"
    @Column(nullable = false)
    private String title;

    // Date of the exam / session
    private LocalDate examDate;

    // Start time (optional)
    private LocalTime startTime;

    // Duration in minutes (optional)
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(length = 1000)
    private String description;

    // Whether this exam is active / upcoming
    @Builder.Default
    private boolean active = true;
}
