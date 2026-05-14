package com.suayb.bpa.controller;

import com.suayb.bpa.dto.AIAnalysisResult;
import com.suayb.bpa.dto.AnalysisResult;
import com.suayb.bpa.dto.UniversalAnalysisResult;
import com.suayb.bpa.model.ProcessCase;
import com.suayb.bpa.service.AIRecommendationService;
import com.suayb.bpa.service.AnalysisService;
import com.suayb.bpa.service.CsvImportService;
import com.suayb.bpa.service.UniversalCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProcessController {

    @Autowired private CsvImportService csvImportService;
    @Autowired private AnalysisService analysisService;
    @Autowired private AIRecommendationService aiRecommendationService;
    @Autowired private UniversalCsvService universalCsvService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResult> analyze(@RequestParam("file") MultipartFile file) {
        try {
            List<ProcessCase> cases = csvImportService.parseCsv(file);
            return ResponseEntity.ok(analysisService.analyze(cases));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/analyze/ai")
    public ResponseEntity<AIAnalysisResult> analyzeWithAI(@RequestParam("file") MultipartFile file) {
        try {
            List<ProcessCase> cases = csvImportService.parseCsv(file);
            AnalysisResult result = analysisService.analyze(cases);
            AIAnalysisResult aiResult = aiRecommendationService.generateRecommendations(
                cases, result.getDepartmentBreakdown(),
                result.getDelayRatePercent(), result.getAvgDelayDays(), result.getOpenCases()
            );
            return ResponseEntity.ok(aiResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/analyze/universal")
    public ResponseEntity<UniversalAnalysisResult> analyzeUniversal(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "lang", defaultValue = "en") String lang) {
        try {
            return ResponseEntity.ok(universalCsvService.analyze(file, lang));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
