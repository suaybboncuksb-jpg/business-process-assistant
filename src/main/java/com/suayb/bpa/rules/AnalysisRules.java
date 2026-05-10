package com.suayb.bpa.rules;

import java.util.ArrayList;
import java.util.List;

public class AnalysisRules {

    public List<String> evaluate(double delayRate, double avgDelay, long openCases) {
        List<String> suggestions = new ArrayList<>();
        if (delayRate > 40) {
            suggestions.add("Warnung: Ueber 40% der Faelle wurden verspaetet abgeschlossen!");
        } else if (delayRate > 20) {
            suggestions.add("Hinweis: " + String.format("%.1f", delayRate) + "% der Faelle sind verzoegert.");
        }
        if (avgDelay > 5) {
            suggestions.add("Durchschnittliche Verzoegerung: " + String.format("%.1f", avgDelay) + " Tage.");
        }
        if (openCases > 5) {
            suggestions.add("Es sind noch " + openCases + " Faelle offen. Priorisierung empfohlen.");
        }
        if (suggestions.isEmpty()) {
            suggestions.add("Prozesse laufen gut. Keine kritischen Auffaelligkeiten.");
        }
        return suggestions;
    }
}
