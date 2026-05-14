package com.suayb.bpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class AIAnalysisResult {
    private String summary;
    private String riskLevel;
    private int overallHealthScore;
    private List<AIRecommendation> recommendations;
    private String predictedTrend;
    private String generatedBy;
}
