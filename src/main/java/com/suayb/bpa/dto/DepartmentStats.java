package com.suayb.bpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentStats {
    private String department;
    private long totalCases;
    private long openCases;
    private long delayedCases;
    private double avgDelayDays;
    private double delayRatePercent;
}
