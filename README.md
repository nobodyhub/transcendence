# Transcendence

Finance Analysis

## Build
Build with command:
```bash
mvn clean jacoco:prepare-agent install jacoco:report sonar:sonar spring-boot:repackage
```

## Run
Run with command(to fetch data for past day, by default will fetch ALL):
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--fetch.size=DAY
``` 
