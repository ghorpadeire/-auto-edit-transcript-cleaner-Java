# Transcript Cleaner Backend

Java-based service for automated transcript cleaning and video edit sequence generation.

## Features
- Linear time $O(n)$ processing.
- Configurable silence detection and filler word removal.
- REST API for integration with NLE plugins.
- FCP7 XML export for Adobe Premiere Pro.

## Setup
1. Build project: `mvn clean package`
2. Run service: `java -jar target/transcript-cleaner-1.0.4.jar`
3. Docker: `docker-compose up --build`

## API Reference
`POST /api/v1/analyze`
Accepts a transcript JSON and returns video clip segments.
