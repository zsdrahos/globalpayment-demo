# AI Usage - Prompt History

The following significant prompts were used during the development of the Global Payment Service to assist with code generation and architectural design.

### Prompts and Topics:

1. Dependency Management:
"What dependencies are needed for a production-ready Spring Boot payment service using Java 21, H2, and requiring resilience for flaky external APIs?"

2. Domain Modeling:
"Create a JPA Entity for a bank Account with balance, currency, and optimistic locking. Also, design an IdempotencyRecord entity to handle X-Idempotency-Key storage."

3. Idempotency Logic:
"Implement a Controller and Service logic that checks for an idempotency key. If the status is PROCESSING, return 409. If SUCCESS, return the previous response. If FAILED, allow retry."

4. External API Resilience:
"How to implement a Feign Client with Resilience4j Retry and Circuit Breaker to handle a 503 error from a mocked exchange rate API?"

5. System Integration:
"How to use Spring ApplicationEventPublisher to notify external domains like Fraud Detection about a successful transfer without tight coupling?"

6. Differences between Java 17 and Java 21:
"What is the key difference between java 17 and java 21?"