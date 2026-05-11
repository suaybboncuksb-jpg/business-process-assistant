# AI Business Process Assistant

A Spring Boot REST API that analyzes business process data from CSV files and generates actionable insights.

## What it does

- Upload a CSV file with business process data
- Calculates KPIs: delay rate, average delay, open cases
- Generates rule-based improvement suggestions
- Returns structured JSON results

## Tech Stack

- Java 17
- Spring Boot 3.5
- OpenCSV
- Lombok
- Maven

## Run the application

./mvnw spring-boot:run

The API starts on http://localhost:8080

## API Usage

POST /api/analyze

Send a CSV file as multipart/form-data with field name "file"

Example Response:
{
  "totalCases": 10,
  "openCases": 4,
  "delayedCases": 7,
  "avgDelayDays": 5.5,
  "delayRatePercent": 70.0,
  "suggestions": ["Warnung: Ueber 40% der Faelle wurden verspaetet abgeschlossen!"]
}

## Project Structure

src/main/java/com/suayb/bpa/
- controller/   REST endpoints
- service/      Business logic
- model/        Data classes
- dto/          Response objects
- rules/        Analysis rules

## Roadmap

- Department-based analysis
- React frontend
- AI-powered recommendations
- Docker deployment

## Author

Suayb - Wirtschaftsinformatik Student
