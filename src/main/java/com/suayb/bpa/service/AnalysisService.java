package com.suayb.bpa.service;

import com.suayb.bpa.dto.AnalysisResult;
import com.suayb.bpa.model.ProcessCase;
import com.suayb.bpa.rules.AnalysisRules;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalysisService {

    public AnalysisResult analyze(List<ProcessCase> cases) {
        long total = cases.size();
        long delayed = cases.stream().filter(c -> c.getDelayDays() > 0).count();
        long open = cases.stream().filter(c -> "OPEN".equalsIgnoreCase(c.getStatus())).count();
        double avgDelay = cases.stream().mapToInt(ProcessCase::getDelayDays).average().orElse(0);
        double delayRate = total > 0 ? (double) delayed / total * 100 : 0;
        List<String> suggestions = new AnalysisRules().evaluate(delayRate, avgDelay, open);
        return new AnalysisResult(total, open, delayed, avgDelay, delayRate, suggestions);
    }
}
