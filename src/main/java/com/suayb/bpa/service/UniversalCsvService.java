package com.suayb.bpa.service;

import com.suayb.bpa.dto.ColumnStats;
import com.suayb.bpa.dto.UniversalAnalysisResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UniversalCsvService {

    public UniversalAnalysisResult analyze(MultipartFile file, String language) throws Exception {
        InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().parse(reader);

        List<String> headers = new ArrayList<>(parser.getHeaderMap().keySet());
        List<CSVRecord> records = parser.getRecords();
        int totalRows = records.size();

        List<Map<String, String>> preview = new ArrayList<>();
        for (int i = 0; i < Math.min(5, records.size()); i++) {
            Map<String, String> row = new LinkedHashMap<>();
            for (String h : headers) { row.put(h, records.get(i).get(h)); }
            preview.add(row);
        }

        List<ColumnStats> numericStats = new ArrayList<>();
        for (String col : headers) {
            List<Double> values = new ArrayList<>();
            for (CSVRecord r : records) {
                try { values.add(Double.parseDouble(r.get(col).replace(",", "."))); }
                catch (Exception ignored) {}
            }
            if (values.size() >= totalRows / 2) {
                double min = values.stream().mapToDouble(d->d).min().orElse(0);
                double max = values.stream().mapToDouble(d->d).max().orElse(0);
                double avg = values.stream().mapToDouble(d->d).average().orElse(0);
                double sum = values.stream().mapToDouble(d->d).sum();
                numericStats.add(new ColumnStats(col, min, max, avg, sum, values.size()));
            }
        }

        String detectedType = detectType(headers);
        List<String> insights = generateInsights(numericStats, totalRows, detectedType, language);

        return new UniversalAnalysisResult(
            file.getOriginalFilename(), totalRows, headers.size(),
            headers, numericStats, preview, detectedType, insights
        );
    }

    private String detectType(List<String> headers) {
        String h = headers.toString().toLowerCase();
        if (h.contains("note") || h.contains("punkt") || h.contains("grade") || h.contains("score")) return "GRADES";
        if (h.contains("umsatz") || h.contains("preis") || h.contains("cost") || h.contains("maliyet")) return "FINANCE";
        if (h.contains("caseid") || h.contains("status") || h.contains("delay")) return "PROCESS";
        return "GENERAL";
    }

    private List<String> generateInsights(List<ColumnStats> stats, int rows, String type, String lang) {
        List<String> insights = new ArrayList<>();
        boolean de = "de".equalsIgnoreCase(lang);

        insights.add(de ? "Datensatz mit " + rows + " Einträgen analysiert." : "Dataset with " + rows + " entries analyzed.");

        if (stats.isEmpty()) {
            insights.add(de ? "Keine numerischen Spalten gefunden." : "No numeric columns found.");
            return insights;
        }

        for (ColumnStats s : stats) {
            if ("GRADES".equals(type) && (s.getColumnName().toLowerCase().contains("note") || s.getColumnName().toLowerCase().contains("grade"))) {
                insights.add(de ? "Durchschnittsnote '" + s.getColumnName() + "': " + String.format("%.2f", s.getAverage()) :
                    "Average grade '" + s.getColumnName() + "': " + String.format("%.2f", s.getAverage()));
                if (s.getAverage() > 3.5) insights.add(de ? "⚠️ Hoher Notendurchschnitt – Förderbedarf prüfen." : "⚠️ High average – consider support.");
                else if (s.getAverage() < 2.0) insights.add(de ? "✅ Sehr guter Notendurchschnitt!" : "✅ Excellent average grade!");
            } else {
                insights.add(de ?
                    "'" + s.getColumnName() + "': Min=" + String.format("%.1f", s.getMin()) + " | Max=" + String.format("%.1f", s.getMax()) + " | Ø=" + String.format("%.1f", s.getAverage()) :
                    "'" + s.getColumnName() + "': Min=" + String.format("%.1f", s.getMin()) + " | Max=" + String.format("%.1f", s.getMax()) + " | Avg=" + String.format("%.1f", s.getAverage()));
            }
        }
        return insights;
    }
}
