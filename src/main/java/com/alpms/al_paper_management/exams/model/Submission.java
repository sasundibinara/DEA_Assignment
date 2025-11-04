package com.alpms.al_paper_management.exams.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to exam session
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_session_id")
    private ExamSession examSession;

    // For now we keep it simple (no direct relation to User entity)
    private String studentName;

    private String studentEmail;

    // Path of uploaded answer script (PDF) stored via FileStorageService
    private String filePath;

    // Mark given to this submission (optional)
    private Double score;

    private LocalDateTime submittedAt;
}
