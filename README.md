# 💰 Discount Service

---

## ✅ Features

- ✅ Kotlin 2.x + Spring Boot 3.x
- 📖 OpenAPI 3.0 (Swagger UI)
- 🐳 Docker support
- 🧪 Gradle-based testing
- 📦 Environment-based configuration

---

## 🏁 Getting Started

### 🔧 Prerequisites

- Java 17+
- Docker & Docker Compose
- Gradle 8+ (or use the included `./gradlew`)

---

## ⚙️ Build & Run

### 🛠 Build the project

```bash
./gradlew clean build
```

### 🛠 Build Docker image
```docker
docker build -t docker-service .
```

### ▶️ Run the Docker container
```
docker run --env-file .env -p 8080:8080 discount-service
```

## 🧪 Running tests
To run all tests use:
```bash
./gradlew test
```
FOR WINDOWS USERS:
```
If Docker Desktop is not configured to start automatically with the system, 
please ensure it is manually started before running the tests, 
as some of them depend on Docker and may fail otherwise.
```

## 📚 API Documentation
When application is running 
[CLICK HERE TO SEE API DOCUMENTATION](http://localhost:8080/swagger-ui.html)

## 🧑‍💻 Author

Maintained by [Krystian Witek](https://pl.linkedin.com/in/kw9531) – Kotlin Backend Developer