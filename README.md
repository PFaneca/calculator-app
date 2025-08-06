#  Calculator App

This project was developed as part of a technical assessment for WIT.  
It is a **RESTful API** built with **Java (Spring Boot)**, organized in two modules:  

- **rest** → Exposes the API endpoints and produces messages to Kafka  
- **calculator** → Consumes Kafka messages and performs the calculations  

The API supports the basic calculator operations: **addition, subtraction, multiplication, and division**, with **arbitrary precision signed decimal numbers**.

---

## Technologies
- Java 21  
- Spring Boot  
- Apache Kafka  
- Maven (multi-module project)  
- Docker + Docker Compose  
- JUnit 5  

---

## Project structure

```text
calculator-app/
│── calculator/   # Calculator module (Kafka consumer + business logic)
│── rest/         # REST API module (Kafka producer + endpoints)
│── docker-compose.yml
│── pom.xml       # Parent POM
```

## How to Run
### Prerequisites
- Java 21+
- Maven 3+
- Docker + Docker Compose
### Step 1 — Start Kafka and Zookeeper
```bash
docker-compose up -d kafka zookeeper
```
### Step 2 — Run the Calculator module
```bash
cd calculator
mvn spring-boot:run
```
### Step 3 — Run the REST module
```bash
cd rest
mvn spring-boot:run
```
The API will be available at:
http://localhost:8080/api

## API Endpoints

### Addition
```http
GET /api/sum?a=10&b=5
Response: { "result": "15" }
```
http://localhost:8080/api/sum?a=10&b=5
### Subtraction
```http
GET /api/subtract?a=10&b=5
Response: { "result": "5" }
```
http://localhost:8080/api/subtract?a=10&b=5
### Multiplication
```http
GET /api/multiply?a=10&b=5
Response: { "result": "50" }
```
http://localhost:8080/api/multiply?a=10&b=5
### Division
```http
GET /api/divide?a=10&b=3
Response: { "result": "3.3333333333" }
```
http://localhost:8080/api/divide?a=10&b=3

### Running tests
``` bash
mvn test
```

## Notes

- Modules communicate through **Apache Kafka**:  
  - REST → produces events  
  - Calculator → consumes and executes operations  

- Configuration is managed via `application.properties` (no XML).  

- The system is designed to run **locally with Spring Boot**; Docker is used only for Kafka/Zookeeper in this version.  
