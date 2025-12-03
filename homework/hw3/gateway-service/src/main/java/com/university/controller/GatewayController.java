package com.university.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GatewayController {

    private final RestTemplate restTemplate;


    @Value("${file.service.url:http://file-storage-service:8081}")
    private String fileServiceUrl;

    @Value("${analysis.service.url:http://analysis-service:8082}")
    private String analysisServiceUrl;

    
    @PostMapping(value = "/works", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadWork(
            @RequestParam("student_name") String studentName,
            @RequestParam("assignment_title") String assignmentTitle,
            @RequestParam("file") MultipartFile file) {

        try {

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("student_name", studentName);
            body.add("assignment_title", assignmentTitle);


            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("file", resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


            String uploadUrl = fileServiceUrl + "/internal/files/upload";
            ResponseEntity<Map> uploadResponse = restTemplate.postForEntity(uploadUrl, requestEntity, Map.class);

            if (uploadResponse.getStatusCode() != HttpStatus.OK || uploadResponse.getBody() == null) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error saving file in File Service");
            }

            Map<String, Object> savedWork = uploadResponse.getBody();
            Long workId = ((Number) savedWork.get("id")).longValue();
            String content = (String) savedWork.get("content");
            String contentHash = (String) savedWork.get("contentHash");


            Map<String, String> analysisPayload = Map.of(
                    "workId", workId.toString(),
                    "content", content,
                    "contentHash", contentHash
            );

            String analysisStartUrl = analysisServiceUrl + "/internal/analysis/start";
            restTemplate.postForLocation(analysisStartUrl, analysisPayload);

            return ResponseEntity.ok(Map.of(
                    "message", "Work uploaded successfully",
                    "work_id", workId,
                    "status", "ANALYSIS_STARTED"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing work: " + e.getMessage());
        }
    }

    
    @GetMapping("/works/{workId}/report")
    public ResponseEntity<?> getReport(@PathVariable Long workId) {
        try {
            String reportUrl = analysisServiceUrl + "/internal/analysis/report/" + workId;
            ResponseEntity<Map> response = restTemplate.getForEntity(reportUrl, Map.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Report not ready or work not found");
            }

            Map<String, Object> report = response.getBody();
            Map<String, Object> result = new HashMap<>();


            result.put("work_id", report.get("workId"));
            result.put("status", report.get("status"));
            result.put("is_plagiarism", report.get("plagiarism"));

            if (Boolean.TRUE.equals(report.get("plagiarism"))) {
                result.put("plagiarism_source_id", report.get("originalWorkId"));
                result.put("verdict", "Plagiarism detected from work ID: " + report.get("originalWorkId"));
            } else {
                result.put("verdict", "Original work");
            }


            result.put("word_cloud_url", "/api/works/" + workId + "/wordcloud");

            return ResponseEntity.ok(result);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Analysis Service is unreachable or report not found.");
        }
    }

    
    @GetMapping(value = "/works/{workId}/wordcloud", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getWordCloud(@PathVariable Long workId) {
        try {
            String wordCloudUrl = analysisServiceUrl + "/internal/analysis/wordcloud/" + workId;
            ResponseEntity<byte[]> response = restTemplate.getForEntity(wordCloudUrl, byte[].class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}