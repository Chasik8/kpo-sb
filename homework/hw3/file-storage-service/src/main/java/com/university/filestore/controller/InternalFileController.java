package com.university.filestore.controller;

import com.university.filestore.model.StudentWork;
import com.university.filestore.repository.StudentWorkRepository;
import com.university.filestore.service.FileStoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/internal/files") 
@RequiredArgsConstructor
public class InternalFileController {

    private final FileStoringService fileStoringService;
    private final StudentWorkRepository studentWorkRepository; 

    
    @PostMapping("/upload")
    public ResponseEntity<StudentWork> uploadFile(
            @RequestParam("student_name") String studentName,
            @RequestParam("assignment_title") String assignmentTitle,
            @RequestParam("file") MultipartFile file) {
        try {
            StudentWork savedWork = fileStoringService.storeFile(studentName, assignmentTitle, file);
            return ResponseEntity.ok(savedWork);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @GetMapping("/{workId}")
    public ResponseEntity<StudentWork> getWorkById(@PathVariable Long workId) {
        return studentWorkRepository.findById(workId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping("/by-hash/{contentHash}/exclude/{workId}")
    public ResponseEntity<StudentWork> getOriginalWorkByHash(
            @PathVariable String contentHash,
            @PathVariable Long workId) {

        return studentWorkRepository.findFirstByContentHashAndIdNotOrderBySubmissionDateAsc(contentHash, workId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}