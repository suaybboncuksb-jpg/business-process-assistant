package com.suayb.bpa.service;

import com.suayb.bpa.dto.AnalysisResult;
import com.suayb.bpa.dto.DepartmentStats;
import com.suayb.bpa.model.ProcessCase;
import com.suayb.bpa.rules.AnalysisRules;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    public AnalysisResult analyze(List<ProcessCase> cases) {
        long total = cases.size();
        long delayed = cases.stream().filter(c -> c.getDelayDays() > 0).count();
        long open = cases.stream().filter(c -> "OPEN".equalsIgnoreCase(c.getStatus())).count();
        double avgDelay = cases.stream().mapToInt(ProcessCase::getDelayDays).average().orElse(0);
        double delayRate = total > 0 ? (double) delayed / total * 100 : 0;

        List<String> suggestions = new AnalysisRules().evaluate(delayRate, avgDelay, open);
        List<DepartmentStats> deptStats = buildDepartmentStats(cases);

        return new AnalysisResult(total, open, delayed, avgDelay, delayRate, suggestions, deptStats);
    }

    private List<DepartmentStats> buildDepartmentStats(List<ProcessCase> cases) {
        Map<String, List<ProcessCase>> byDept = cases.stream()
                .collect(Collectors.groupingBy(ProcessCase::getDepartment));

        return byDept.entrySet().stream().map(entry -> {
            String dept = entry.getKey();
            List<ProcessCase> deptCases = entry.getValue();
            long total = deptCases.size();
            long delayed = deptCases.stream().filter(c -> c.getDelayDays() > 0).count();
            long open = deptCases.stream().filter(c -> "OPEN".equalsIgnoreCase(c.getStatus())).count();
            double avgDelay = deptCases.stream().mapToInt(ProcessCase::getDelayDays).average().orElse(0);
            double delayRate = total > 0 ? (double) delayed / total * 100 : 0;
            return new DepartmentStats(dept, total, open, delayed, avgDelay, delayRate);
        }).collect(Collectors.toList());
    }
}
