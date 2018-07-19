# Transcendence Data Repository

Transcendence Data Analyzer

## Required Properties
|Property|Remark|Example|
|:------:|:----:|:-----:|
|cassandra.host|List of contact points for Cassandra, required by `transcendence-data-repository`|192.168.1.75,192.168.1.73|
|cassandra.port|Port for contact points, required by `transcendence-data-repository`|9042|
|cassandra.keyspace|Keyspace of Cassandra Session, required by `transcendence-data-repository`|test|
|stratagy.fetch.start|the start of data to be fetched for analysis|2018-01-01|
|stratagy.fetch.batch|the number of records fetched each time from database|10|
