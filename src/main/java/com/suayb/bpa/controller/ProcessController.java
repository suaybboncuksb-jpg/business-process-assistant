package com.suayb.bpa.controller;

import com.suayb.bpa.dto.AnalysisResult;
import com.suayb.bpa.service.AnalysisService;
import com.suayb.bpa.service.CsvImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProcessController {

    @Autowired private CsvImportService csvImportService;
    @Autowired private AnalysisService analysisService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResult> analyze(@RequestParam("file") MultipartFile file) {
        try {
            var cases = csvImportService.parseCsv(file);
            var result = analysisService.analyze(cases);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
