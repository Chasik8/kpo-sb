package com.university.filestore.repository;

import com.university.filestore.model.StudentWork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentWorkRepository extends JpaRepository<StudentWork, Long> {
    
    Optional<StudentWork> findFirstByContentHashAndIdNotOrderBySubmissionDateAsc(String contentHash, Long id);
}