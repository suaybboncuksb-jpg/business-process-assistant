package com.suayb.bpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class AnalysisResult {
    private long totalCases;
    private long openCases;
    private long delayedCases;
    private double avgDelayDays;
    private double delayRatePercent;
    private List<String> suggestions;
}
