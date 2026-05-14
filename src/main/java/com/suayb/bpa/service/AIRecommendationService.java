package com.suayb.bpa.service;

import com.suayb.bpa.dto.AIAnalysisResult;
import com.suayb.bpa.dto.AIRecommendation;
import com.suayb.bpa.dto.DepartmentStats;
import com.suayb.bpa.model.ProcessCase;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AIRecommendationService {

    public AIAnalysisResult generateRecommendations(
            List<ProcessCase> cases,
            List<DepartmentStats> deptStats,
            double delayRate,
            double avgDelay,
            long openCases) {

        List<AIRecommendation> recommendations = new ArrayList<>();

        analyzeDelayRate(recommendations, delayRate, avgDelay);
        analyzeOpenCases(recommendations, openCases, cases.size());
        analyzeWorstDepartment(recommendations, deptStats);
        analyzePriorityDistribution(recommendations, cases);
        analyzeBestDepartment(recommendations, deptStats);

        int healthScore = calculateHealthScore(delayRate, avgDelay, openCases, cases.size());
        String riskLevel = determineRiskLevel(healthScore);
        String trend = predictTrend(delayRate, avgDelay);
        String summary = generateSummary(cases.size(), delayRate, healthScore, riskLevel);

        return new AIAnalysisResult(
                summary,
                riskLevel,
                healthScore,
                recommendations,
                trend,
                "AI Recommendation Engine v1.0"
        );
    }

    private void analyzeDelayRate(List<AIRecommendation> recs, double delayRate, double avgDelay) {
        if (delayRate > 60) {
            recs.add(new AIRecommendation(
                "Process Efficiency",
                "CRITICAL",
                "Over " + String.format("%.0f", delayRate) + "% of cases are delayed. This indicates a systemic process breakdown requiring immediate intervention.",
                "Conduct an emergency process audit. Identify and eliminate the top 3 bottlenecks within 2 weeks. Consider temporary resource reallocation.",
                94
            ));
        } else if (delayRate > 30) {
            recs.add(new AIRecommendation(
                "Process Efficiency",
                "HIGH",
                String.format("%.0f", delayRate) + "% delay rate detected. Average delay of " + String.format("%.1f", avgDelay) + " days suggests capacity or prioritization issues.",
                "Implement daily standup meetings for delayed cases. Review resource allocation and introduce SLA monitoring.",
                87
            ));
        } else if (delayRate > 10) {
            recs.add(new AIRecommendation(
                "Process Efficiency",
                "MEDIUM",
                "Moderate delay rate of " + String.format("%.0f", delayRate) + "%. Early intervention can prevent escalation.",
                "Set up automated alerts for cases approaching due dates. Review workload distribution across team members.",
                79
            ));
        }
    }

    private void analyzeOpenCases(List<AIRecommendation> recs, long openCases, int total) {
        double openRate = (double) openCases / total * 100;
        if (openRate > 50) {
            recs.add(new AIRecommendation(
                "Workload Management",
                "HIGH",
                openRate > 50 ? "More than half of all cases remain open. This backlog will compound delays over time." : "Significant open case backlog detected.",
                "Implement a case prioritization matrix (Priority x Age). Consider bringing in additional resources or redistributing work.",
                88
            ));
        } else if (openCases > 3) {
            recs.add(new AIRecommendation(
                "Workload Management",
                "MEDIUM",
                openCases + " cases currently open. Monitor closely to prevent backlog buildup.",
                "Assign dedicated owners to each open case. Set clear resolution deadlines and track daily progress.",
                75
            ));
        }
    }

    private void analyzeWorstDepartment(List<AIRecommendation> recs, List<DepartmentStats> deptStats) {
        deptStats.stream()
            .filter(d -> d.getDelayRatePercent() > 0)
            .max(Comparator.comparingDouble(DepartmentStats::getDelayRatePercent))
            .ifPresent(worst -> {
                if (worst.getDelayRatePercent() > 50) {
                    recs.add(new AIRecommendation(
                        "Department Risk",
                        "HIGH",
                        "Department '" + worst.getDepartment() + "' shows " + String.format("%.0f", worst.getDelayRatePercent()) + "% delay rate with avg " + String.format("%.1f", worst.getAvgDelayDays()) + " days delay.",
                        "Schedule an immediate review meeting with '" + worst.getDepartment() + "' leadership. Map their current process flow and identify specific pain points.",
                        91
                    ));
                }
            });
    }

    private void analyzeBestDepartment(List<AIRecommendation> recs, List<DepartmentStats> deptStats) {
        deptStats.stream()
            .filter(d -> d.getDelayRatePercent() == 0 && d.getTotalCases() > 1)
            .findFirst()
            .ifPresent(best -> {
                recs.add(new AIRecommendation(
                    "Best Practice",
                    "POSITIVE",
                    "Department '" + best.getDepartment() + "' achieves 0% delay rate across " + best.getTotalCases() + " cases. Their process is a benchmark.",
                    "Document '" + best.getDepartment() + "' workflows and create internal best practice guides. Consider cross-training other departments.",
                    96
                ));
            });
    }

    private void analyzePriorityDistribution(List<AIRecommendation> recs, List<ProcessCase> cases) {
        long highPriority = cases.stream()
            .filter(c -> "HIGH".equalsIgnoreCase(c.getPriority()))
            .count();
        long highDelayed = cases.stream()
            .filter(c -> "HIGH".equalsIgnoreCase(c.getPriority()) && c.getDelayDays() > 0)
            .count();

        if (highPriority > 0 && highDelayed > 0) {
            double highDelayRate = (double) highDelayed / highPriority * 100;
            if (highDelayRate > 30) {
                recs.add(new AIRecommendation(
                    "Priority Management",
                    "HIGH",
                    String.format("%.0f", highDelayRate) + "% of HIGH priority cases are delayed. Critical work is not being treated with appropriate urgency.",
                    "Implement a strict SLA policy for HIGH priority cases. Daily escalation reporting for any HIGH priority case delayed more than 1 day.",
                    89
                ));
            }
        }
    }

    private int calculateHealthScore(double delayRate, double avgDelay, long openCases, int total) {
        int score = 100;
        score -= (int)(delayRate * 0.5);
        score -= (int)(avgDelay * 3);
        double openRate = (double) openCases / total * 100;
        score -= (int)(openRate * 0.2);
        return Math.max(0, Math.min(100, score));
    }

    private String determineRiskLevel(int healthScore) {
        if (healthScore >= 80) return "LOW";
        if (healthScore >= 60) return "MEDIUM";
        if (healthScore >= 40) return "HIGH";
        return "CRITICAL";
    }

    private String predictTrend(double delayRate, double avgDelay) {
        if (delayRate > 60 && avgDelay > 7) return "DETERIORATING - Immediate action required";
        if (delayRate > 30) return "AT RISK - Close monitoring needed";
        if (delayRate > 10) return "STABLE - Minor improvements recommended";
        return "POSITIVE - Maintain current practices";
    }

    private String generateSummary(int total, double delayRate, int healthScore, String riskLevel) {
        return "Analysis of " + total + " process cases reveals a " + riskLevel + " risk profile with an overall health score of " +
               healthScore + "/100. " + String.format("%.0f", delayRate) + "% delay rate identified. " +
               (healthScore < 60 ? "Immediate management intervention recommended." : "Targeted improvements can optimize performance.");
    }
}
