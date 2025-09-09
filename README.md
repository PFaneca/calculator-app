#  Calculator App

This project was developed to simulate a real backend development scenario, focusing on building and integrating microservices with Java, Spring Boot, REST APIs, Apache Kafka, and Docker. 
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

## Architecture & Design Choices

This project implements a **Request-Reply pattern with Kafka**, simulating synchronous behavior over an asynchronous messaging system:

1. **REST API (Producer)**  
   - Receives client requests, generates a correlation ID, and publishes messages to Kafka (`calc-requests`).

2. **Calculator Service (Consumer)**  
   - Listens for requests, executes the operation, and publishes the result to Kafka (`calc-results`) with the same correlation ID.

3. **REST API (Result Listener)**  
   - Listens for results and matches correlation IDs to complete the original HTTP request.

**Key design benefits:**

- Demonstrates **decoupled microservices** using Kafka  
- Shows handling of **correlation IDs** for request-response tracking  
- Simulates **synchronous REST response over asynchronous messaging**  
- Easy to extend: support for new operations, logging, monitoring, retries  

>  In production, Kafka is usually used for asynchronous processing (fire-and-forget).  
> The Request-Reply pattern here is used to demonstrate correlation, async processing, and synchronous API handling.

## How to Run
### Prerequisites
- Java 21+
- Maven 3+
- Docker + Docker Compose

### Option 1 — Run services manually
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
### Option 2 — Run all services with Docker
- You can start Kafka, Zookeeper, Calculator, and REST in a single command using Docker Compose:
```bash
docker-compose up -d
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

- The system is designed to run **locally with Spring Boot**.

- Request-Reply flow:
  - REST sends request with correlation ID → Calculator consumes → Calculator publishes result → REST matches correlation ID → client receives response.   

- Docker: All services can be run together with Docker Compose for easy local testing and demos.
