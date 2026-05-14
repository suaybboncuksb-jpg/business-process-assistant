package com.suayb.bpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnStats {
    private String columnName;
    private double min;
    private double max;
    private double average;
    private double sum;
    private int count;
}
