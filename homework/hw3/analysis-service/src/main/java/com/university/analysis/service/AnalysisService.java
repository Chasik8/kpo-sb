package com.university.analysis.service;

import com.university.analysis.model.AnalysisReport;
import com.university.analysis.repository.AnalysisReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final AnalysisReportRepository reportRepository;
    private final RestTemplate restTemplate;


    @Value("${file.service.url:http://file-storage-service:8081}")
    private String fileServiceUrl;

    @Async
    public void performAnalysis(Long workId, String textContent, String contentHash) {

        AnalysisReport report = AnalysisReport.builder()
                .workId(workId)
                .status("PROCESSING")
                .isPlagiarism(false)
                .build();
        report = reportRepository.save(report);

        try {

            String plagiarismCheckUrl = String.format("%s/internal/files/by-hash/%s/exclude/%d",
                    fileServiceUrl, contentHash, workId);

            try {
                ResponseEntity<Map> originalWorkResponse = restTemplate.getForEntity(plagiarismCheckUrl, Map.class);

                if (originalWorkResponse.getStatusCode() == HttpStatus.OK && originalWorkResponse.getBody() != null) {

                    Long originalId = ((Number) originalWorkResponse.getBody().get("id")).longValue();

                    report.setPlagiarism(true);
                    report.setOriginalWorkId(originalId);
                }
            } catch (Exception e) {

                System.err.println("File Service call failed (may be 404 or service down): " + e.getMessage());
            }


            byte[] wordCloud = generateWordCloud(textContent);
            report.setWordCloudImage(wordCloud);

            report.setStatus("COMPLETED");
        } catch (Exception e) {
            report.setStatus("ERROR");

        }

        reportRepository.save(report);
    }

    
    private byte[] generateWordCloud(String text) {
        try {
            String apiUrl = "https://quickchart.io/wordcloud";
            Map<String, Object> payload = new HashMap<>();
            payload.put("text", text);
            payload.put("format", "png");
            payload.put("width", 800);
            payload.put("height", 600);
            payload.put("removeStopwords", true);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<byte[]> response = restTemplate.postForEntity(apiUrl, request, byte[].class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Failed to generate word cloud: " + e.getMessage());
            return new byte[0];
        }
    }
}