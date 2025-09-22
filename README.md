# Used_Book

Lightweight Spring Boot application that manages a used-books inventory. This project demonstrates a small service layer, JPA entity modeling, a simple pricing decorator (depreciation), validation, and centralized error handling.

## Contents

- `src/main/java/...` - application source
- `src/test` - tests (uses in-memory H2 database)
- `pom.xml` - Maven build file

## Requirements

- Java 17 (project target)
- Maven (the repository includes the Maven wrapper `./mvnw`)
- (Optional) MySQL if you want to run against a MySQL database — the project defaults to MySQL in `src/main/resources/application.properties`.

Recommended local Java: set your JAVA_HOME to a Java 17 JDK or let the maven-compiler plugin handle compilation with `release` set to 17.

## Quick start (development)

1. Open a terminal in the project root:

```bash
cd /Users/shreyaskullarni/Documents/ood/Used_Book
```

2. Run the application with the Maven wrapper:

```bash
./mvnw spring-boot:run
```

By default the app will start on port 8080. You can change server settings with `application.properties` or profiles.

## Build

Create a runnable JAR:

```bash
./mvnw clean package
# The resulting jar will be in target/Used_Book-0.0.1-SNAPSHOT.jar
java -jar target/Used_Book-0.0.1-SNAPSHOT.jar
```

## Running tests

Unit tests use an in-memory H2 database (configuration located in `src/test/resources/application.properties`). Run:

```bash
./mvnw test
```

## Configuration

- Application config for development: `src/main/resources/application.properties` (uses MySQL by default)
- Test config for unit tests: `src/test/resources/application.properties` (uses H2 in memory)

If you want to run against MySQL, update `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` in `application.properties` or use environment variables:

```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/used_book_db?useSSL=false&serverTimezone=UTC"
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD="your_password"
./mvnw spring-boot:run
```

Security note: avoid committing raw passwords to source control. Use environment variables or a secure secrets manager in production.

## API

Base URL: `http://localhost:8080`

Health/Home
- `GET /` — returns a short running message.

Books
- `GET /books` — returns a JSON array of books in inventory.

- `POST /books/sell` — register a brand-new book in inventory (creates a new copy). Body (JSON):

```json
{
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "edition": "3rd",
  "price": 45.00
}
```

Response (201 Created):
```json
{ "status": "success", "price": 45.0 }
```

- `POST /books/buy/{id}` — simulate buying a book (removes it from inventory). Path param `id` is the book's UUID.

Response (200 OK):
```json
{ "status": "success", "id": "<uuid>" }
```

- `POST /books/sell/{id}` — resell an existing copy by id. This applies the depreciation decorator (10% reduction) and creates a new record with a new id.

Response (201 Created):
```json
{ "status": "success", "price": 40.5 }
```

- `POST /books/sell/isbn/{isbn}` — find a copy by ISBN, depreciate and add a new copy (useful when you only know the ISBN).

Response (201 Created):
```json
{ "status": "success", "price": 40.5 }
```

Validation and errors
- The application uses Jakarta Bean Validation for incoming book payloads. Invalid requests return 400 with a JSON body like:

```json
{
  "timestamp": "2025-09-22T...",
  "status": 400,
  "error": "Validation Failed",
  "message": ["price: Price must be positive", "isbn: ISBN must not be blank"]
}
```

- Not found errors (e.g., selling/buying a non-existent book) return 404 with a structured JSON error (timestamp, status, error, message).

## Example curl requests

Add a new book:

```bash
curl -s -X POST http://localhost:8080/books/sell \
  -H "Content-Type: application/json" \
  -d '{"isbn":"9780134685991","title":"Effective Java","author":"Joshua Bloch","edition":"3rd","price":45.0}' | jq
```

List books:

```bash
curl -s http://localhost:8080/books | jq
```

Buy (remove) a book by id:

```bash
curl -X POST http://localhost:8080/books/buy/<BOOK_ID>
```

Resell a book by id (depreciates by 10% and creates a new copy):

```bash
curl -X POST http://localhost:8080/books/sell/<BOOK_ID>
```

Resell by ISBN:

```bash
curl -X POST http://localhost:8080/books/sell/isbn/9780134685991
```

## Contributing

Contributions are welcome. Typical workflow:

1. Fork the repository on GitHub
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Make changes and add tests
4. Run `./mvnw test`
5. Open a pull request with a description of your change

## Next improvements (ideas)

- Introduce DTOs for API layer and map to JPA entities (prevents leaking internal models)
- Add OpenAPI/Swagger for interactive API docs
- Add integration tests using Testcontainers + MySQL to test the production DB behaviour
- Add pagination/filtering to `GET /books`

## License

This project currently has no license file. Add a `LICENSE` file to set a license (MIT, Apache-2.0, etc.).

---

If you want, I can also:
- Add a `README` badge and OpenAPI docs
- Create a minimal `README` with examples for GitHub (shorter)
- Add a `LICENSE` file (MIT/Apache) and commit it

Let me know which of those you'd like next and I will add it.
