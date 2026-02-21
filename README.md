# Global Payment Service

This is a production-ready Payment Gateway service built with Spring Boot 3.x and Java 21. It manages user accounts and processes fund transfers with a focus on reliability, data integrity, and idempotency in distributed systems.

## Tech Stack
* Language: Java 21
* Framework: Spring Boot 3.3.x
* Database: H2 (In-memory)
* Communication: Spring Cloud OpenFeign
* Resilience: Resilience4j (Retry, Circuit Breaker)
* Build Tool: Maven
* Utilities: Lombok

---

## Architectural Choices

### 1. Idempotency Implementation
To prevent double-charging during network retries, every POST /api/transfers request requires an X-Idempotency-Key header.
* Persistence: Idempotency keys are stored in a dedicated database table (idempotency_keys).
* State Machine: Each request transitions through states: PROCESSING, SUCCESS, or FAILED.
* Logic: 
  - If a request is PROCESSING, the system returns 409 Conflict.
  - If SUCCESS, the cached original response is returned.
  - If FAILED, the user is allowed to retry the transaction.

### 2. Concurrency and Data Integrity
* Optimistic Locking: The Account entity uses a @Version field. This prevents the "Lost Update" problem when multiple concurrent transfers target the same account.
* Atomic Transactions: All balance adjustments are wrapped in @Transactional blocks to ensure ACID properties.

### 3. Resilience to External Services
The external Exchange Rate API is treated as an unreliable dependency.
* Retry Pattern: Automatically retries on 503 Service Unavailable errors using exponential backoff.
* Circuit Breaker: Prevents cascading failures by stopping calls to the external service if it becomes consistently unstable.

### 4. System Integration
The service utilizes Spring's ApplicationEventPublisher to trigger a TransferSuccessfulEvent. This allows other domains, such as Fraud Detection or Notifications, to remain decoupled from the core transfer logic.

---

## API Endpoints

### Money Transfer
POST /api/transfers

Headers:
* X-Idempotency-Key: (String)
* Content-Type: application/json

Body:
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 100.00,
  "currency": "USD"
}

Responses:
* 201 Created: Transfer successful.
* 409 Conflict: Request is already being processed.
* 400 Bad Request: Insufficient funds or validation error.
* 503 Service Unavailable: External API failed after retries.

---

## Running the Application

Prerequisites:
* Java 21
* Maven

Commands:
mvn clean install
mvn spring-boot:run

H2 Console:
* URL: http://localhost:8080/h2-console
* JDBC URL: jdbc:h2:mem:paymentdb
* User: sa
* Password: (empty)

---

## AI Usage Policy 
Google Gemini AI - AI tools were used to assist with architectural decisions, boilerplate generation, and resilience configuration. Detailed interaction history is documented in PROMPTS.md.