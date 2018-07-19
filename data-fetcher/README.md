# Transcendence Data Fetcher

Data Fetcher for Financial Information

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

## Required Properties

|Property|Remark|Example|
|:------:|:----:|:-----:|
|cassandra.host|List of contact points for Cassandra, required by `transcendence-data-repository`|192.168.1.75,192.168.1.73|
|cassandra.port|Port for contact points, required by `transcendence-data-repository`|9042|
|cassandra.keyspace|Keyspace of Cassandra Session, required by `transcendence-data-repository`|test|
|stratagy.fetch.start|the start of data to be fetched for analysis|2018-01-01|
|stratagy.fetch.batch|the number of records fetched each time from database|10|
|fetch.size|the size of data to be fetched, @see FetchSize|ALL|
