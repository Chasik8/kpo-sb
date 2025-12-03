package com.university.analysis.controller;

import com.university.analysis.service.AnalysisService;
import com.university.analysis.model.AnalysisReport;
import com.university.analysis.repository.AnalysisReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/internal/analysis")
@RequiredArgsConstructor
public class InternalAnalysisController {
    private final AnalysisService analysisService;
    private final AnalysisReportRepository repository;

    
    @PostMapping("/start")
    public ResponseEntity<Void> startAnalysis(@RequestBody Map<String, String> payload) {
        Long workId = Long.parseLong(payload.get("workId"));
        String content = payload.get("content");
        String hash = payload.get("contentHash");


        analysisService.performAnalysis(workId, content, hash);
        return ResponseEntity.accepted().build();
    }

    
    @GetMapping("/report/{workId}")
    public ResponseEntity<AnalysisReport> getReport(@PathVariable Long workId) {
        return repository.findByWorkId(workId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @GetMapping(value = "/wordcloud/{workId}", produces = "image/png")
    public ResponseEntity<byte[]> getWordCloud(@PathVariable Long workId) {
        return repository.findByWorkId(workId)
                .filter(report -> report.getWordCloudImage() != null && report.getWordCloudImage().length > 0)
                .map(report -> ResponseEntity.ok().body(report.getWordCloudImage()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}