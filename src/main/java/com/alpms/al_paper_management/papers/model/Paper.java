package com.alpms.al_paper_management.papers.model;



import com.alpms.al_paper_management.subjects.model.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "papers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paper {

    public enum Type { PAST, MODEL }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false, length = 180)
    private String title;

    @NotNull @Column(nullable = false)
    private Integer year;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Type type = Type.PAST;

    // FK -> subjects.id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotBlank @Column(nullable = false)
    private String filePath; // stored on disk (uploads/papers/...)
}
