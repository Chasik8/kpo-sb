package com.university.filestore.service;

import com.university.filestore.model.StudentWork;
import com.university.filestore.repository.StudentWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileStoringService {

    private final StudentWorkRepository workRepository;

    
    public StudentWork storeFile(String studentName, String assignment, MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String hash = calculateHash(content);

        StudentWork work = StudentWork.builder()
                .studentName(studentName)
                .assignmentTitle(assignment)
                .submissionDate(LocalDateTime.now())
                .content(content)
                .contentHash(hash)
                .build();

        return workRepository.save(work);
    }

    
    private String calculateHash(String content) throws NoSuchAlgorithmException {

        String normalized = content.replaceAll("\\s+", "").toLowerCase();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(normalized.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (byte b : encodedhash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}