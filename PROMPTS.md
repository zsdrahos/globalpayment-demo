# AI Prompt History


## 1. Architectural Design & Patterns
* **Prompt:** "What is an X-Idempotency-Key and what is the best way to handle it in a distributed payment system using Spring Boot"
    * **Result:** Result: The AI explained the concept of idempotency (ensuring an operation happens only once) and suggested a database-backed state machine (Processing/Success/Failed).
    * **My Implementation:** Result: The AI explained the concept of idempotency (ensuring an operation happens only once) and suggested a database-backed state machine (Processing/Success/Failed).

## 2. Resilience & Error Handling
* **Prompt:** "What is and how to implement a Feign Client with Resilience4j Retry and Circuit Breaker for a flaky external API?"
    * **Result:** Provided the configuration structure for `application.properties` and the `@CircuitBreaker` annotation usage.
    * **My Implementation:** I fine-tuned the retry attempts and wait durations to match the "flaky API" requirement.

## 3. Concurrency & Data Integrity
* **Prompt:** "Explain how @Version prevents lost updates in a bank transfer scenario."
    * **Result:** Explained Optimistic Locking.
    * **My Implementation:** I added the versioning to the `Account` entity to ensure that concurrent transfers to the same account do not result in incorrect balances.

## 4. Troubleshooting (Manual Refinement)
* **Issue:** I encountered a "Reserved Keyword" error with the H2 database when using `KEY` as a column name.
* **Process:** I used AI to identify that `KEY` is reserved in H2 and manually applied the `@Column(name = "idempotency_key")` fix.

## 5. Documentation
* **Issue:** Generate a professional README.md including build instructions, and a PROMPTS.md with the asked questions.
