# AI Business Process Assistant

An intelligent business process analysis tool built with Java and Spring Boot.
Upload any CSV file and get instant AI-powered insights, KPIs, and recommendations.

## Live Features

- Upload any CSV file (process data, grades, finance data)
- Automatic dataset type detection (PROCESS / GRADES / FINANCE / GENERAL)
- KPI calculation: delay rate, average delay, open cases
- Department-based risk analysis
- AI Recommendation Engine with confidence scores
- Process health score (0-100)
- Trend prediction
- Interactive HTML dashboard
- DE / EN language switch
- Swagger UI API documentation

## Tech Stack

- Java 17
- Spring Boot 3.5
- OpenCSV + Apache Commons CSV
- Lombok
- Maven
- Vanilla HTML/CSS/JavaScript Frontend
- Swagger UI (OpenAPI 3.0)

## How to run

./mvnw spring-boot:run

App starts on http://localhost:8080

## API Endpoints

POST /api/analyze              - Standard process analysis
POST /api/analyze/ai           - AI deep analysis with recommendations
POST /api/analyze/universal    - Universal analysis for any CSV format

API Documentation: http://localhost:8080/swagger-ui/index.html

## Example Response (AI Analysis)

{
  "summary": "Analysis of 10 process cases reveals a HIGH risk profile with health score 41/100.",
  "riskLevel": "HIGH",
  "overallHealthScore": 41,
  "predictedTrend": "AT RISK - Close monitoring needed",
  "recommendations": [
    {
      "category": "Process Efficiency",
      "severity": "CRITICAL",
      "insight": "Over 70% of cases are delayed.",
      "action": "Conduct an emergency process audit within 2 weeks.",
      "confidenceScore": 94
    }
  ]
}

## Project Structure

src/main/java/com/suayb/bpa/
- controller/    REST endpoints (ProcessController)
- service/       Business logic (AnalysisService, AIRecommendationService, UniversalCsvService)
- model/         Data classes (ProcessCase)
- dto/           Response objects (AnalysisResult, AIAnalysisResult, UniversalAnalysisResult)
- rules/         Rule engine (AnalysisRules)

## Roadmap

- React frontend
- Database integration
- Docker deployment
- Real AI API integration (OpenAI / Claude)

## Author

Suayb - Wirtschaftsinformatik Student
GitHub: https://github.com/suaybboncuksb-jpg
