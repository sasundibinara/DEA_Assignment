package com.alpms.al_paper_management.subjects.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "subjects")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Subject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject name is required")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Stream stream = Stream.TECHNOLOGY; // default

    public enum Stream {
        TECHNOLOGY, SCIENCE, MATHEMATICS, COMMERCE, ARTS
    }
}
