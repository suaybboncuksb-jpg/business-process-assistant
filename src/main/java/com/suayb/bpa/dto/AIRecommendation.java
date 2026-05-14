package com.suayb.bpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AIRecommendation {
    private String category;
    private String severity;
    private String insight;
    private String action;
    private int confidenceScore;
}
