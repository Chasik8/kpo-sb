package com.university.analysis.repository;

import com.university.analysis.model.AnalysisReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnalysisReportRepository extends JpaRepository<AnalysisReport, Long> {
    Optional<AnalysisReport> findByWorkId(Long workId);
}