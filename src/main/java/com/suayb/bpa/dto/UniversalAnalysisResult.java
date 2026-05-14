package com.suayb.bpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class UniversalAnalysisResult {
    private String datasetName;
    private int totalRows;
    private int totalColumns;
    private List<String> columns;
    private List<ColumnStats> numericStats;
    private List<Map<String, String>> preview;
    private String detectedType;
    private List<String> insights;
}
