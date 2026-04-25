# Bootcamp Student Service Scala

Bootcamp Student Service Scala is a small Scala 3 backend service created for learning and experimenting with service design, configuration, logging, testing, and basic HTTP API development.

## Tech Stack

- Scala 3
- sbt
- http4s
- PureConfig
- log4cats
- Logback
- MUnit Cats Effect

## Run the Project

### Run locally

~~~bash
sbt run
~~~

The application starts on:

~~~text
http://localhost:8080
~~~

## Run tests

~~~bash
sbt test
~~~

## Available Endpoints

### Health check

~~~http
GET /healthcheck
~~~

Example:

~~~bash
curl http://localhost:8080/healthcheck
~~~

### Student endpoints

~~~http
POST   /students
GET    /students
GET    /students/{id}
PUT    /students/{id}
DELETE /students/{id}
~~~

## Example Student JSON

~~~json
{
  "firstName": "Ivan",
  "lastName": "Petrenko",
  "email": "ivan.petrenko@example.com"
}
~~~

## Create a Student

The service uses in-memory storage, so students are cleared after application restart.

~~~bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ivan",
    "lastName": "Petrenko",
    "email": "ivan.petrenko@example.com"
  }'
~~~

~~~bash
curl http://localhost:8080/students
~~~

## Project Goal

This project is used as a minimal Student Service for practicing:

- service structure
- API design
- configuration management
- logging
- testing
- further extension in future practical tasks

## Documentation

- [Student Service Requirements and Limitations](docs/requirements.md)