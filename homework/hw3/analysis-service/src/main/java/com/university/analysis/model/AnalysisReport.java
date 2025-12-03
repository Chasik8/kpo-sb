package com.university.analysis.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "analysis_reports")
public class AnalysisReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workId; 
    private boolean isPlagiarism;
    private Long originalWorkId; 

    @Lob
    private byte[] wordCloudImage;

    private String status; 
}