package com.suayb.bpa.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessCase {

    @CsvBindByName(column = "caseId")
    private String caseId;

    @CsvBindByName(column = "department")
    private String department;

    @CsvBindByName(column = "status")
    private String status;

    @CsvBindByName(column = "priority")
    private String priority;

    @CsvBindByName(column = "createdDate")
    private String createdDate;

    @CsvBindByName(column = "dueDate")
    private String dueDate;

    @CsvBindByName(column = "completedDate")
    private String completedDate;

    @CsvBindByName(column = "delayDays")
    private int delayDays;
}
