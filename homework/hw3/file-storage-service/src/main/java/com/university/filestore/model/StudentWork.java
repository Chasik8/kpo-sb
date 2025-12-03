package com.university.filestore.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student_works")
public class StudentWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String assignmentTitle;
    private LocalDateTime submissionDate;

    @Column(columnDefinition = "TEXT")
    private String content; 

    private String contentHash; 
}