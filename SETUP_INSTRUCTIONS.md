# Task Manager - Setup Instructions

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Git

### 1. Clone and Setup
```bash
# Clone the repository
git clone <your-repo-url>
cd task-manager

# Build the application
mvn clean install
```

### 2. Run the Application
```bash
# Start the application
mvn spring-boot:run

# Access the application
open http://localhost:8080
```

## 🧪 Testing

### Run All Tests
```bash
# Unit tests only
mvn test

# Integration tests
mvn verify

# All tests with coverage report
mvn clean test jacoco:report
```

### Test Coverage
- **Unit Tests**: 26 tests covering models, services, and validation
- **Integration Tests**: 12 tests covering web layer and HTTP endpoints
- **Coverage Report**: Available at `target/site/jacoco/index.html`

### Code Quality Checks
```bash
# Run all quality checks
mvn clean verify spotbugs:check checkstyle:check pmd:check

# Individual checks
mvn spotbugs:check    # Bug detection
mvn checkstyle:check  # Code style
mvn pmd:check        # Code analysis
```

## 🔄 CI/CD Pipeline

### GitHub Actions Workflow
The project includes a comprehensive CI/CD pipeline that runs on:
- ✅ Every push to any branch
- ✅ Every pull request to main/develop
- ✅ Manual triggers

### Pipeline Stages
1. **🧪 Tests** - Runs on Java 17 & 21
2. **🏗️ Build** - Compiles and packages
3. **🔒 Security Scan** - OWASP dependency check
4. **📊 Code Quality** - SpotBugs, Checkstyle, PMD
5. **🐳 Docker Build** - Creates container (main branch only)
6. **📢 Notifications** - Reports results

### Local Pipeline Simulation
```bash
# Simulate the full CI/CD pipeline locally
mvn clean verify spotbugs:check checkstyle:check pmd:check
```

## 📁 Project Structure

```
task-manager/
├── src/
│   ├── main/
│   │   ├── java/com/taskmanager/
│   │   │   ├── TaskManagerApplication.java
│   │   │   ├── controller/
│   │   │   │   └── TaskController.java
│   │   │   ├── model/
│   │   │   │   ├── Task.java
│   │   │   │   ├── TaskStatus.java
│   │   │   │   └── TaskPriority.java
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.java
│   │   │   └── service/
│   │   │       └── TaskService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/
│   │       │   └── index.html
│   │       └── static/
│   │           ├── css/style.css
│   │           └── js/app.js
│   └── test/
│       ├── java/com/taskmanager/
│       │   ├── TaskManagerApplicationTests.java
│       │   ├── model/
│       │   │   └── TaskTest.java
│       │   ├── service/
│       │   │   └── TaskServiceTest.java
│       │   └── controller/
│       │       └── TaskControllerIntegrationTest.java
│       └── resources/
│           └── application-test.properties
├── .github/workflows/
│   └── ci.yml
├── pom.xml
├── Dockerfile
├── checkstyle.xml
├── .gitignore
├── LICENSE
└── README.md
```

## 🐳 Docker Support

### Build Docker Image
```bash
# Build the application first
mvn clean package

# Build Docker image
docker build -t task-manager .

# Run the container
docker run -p 8080:8080 task-manager
```

## 🔧 Configuration

### Application Properties
- **Database**: H2 in-memory/file database
- **Port**: 8080
- **H2 Console**: Available at `/h2-console`
- **Profiles**: `default`, `test`

### Test Configuration
- **Database**: H2 in-memory database
- **Profile**: `test`
- **Coverage**: JaCoCo enabled

## 📊 Test Results

### Current Test Status
- ✅ **Unit Tests**: 26/26 passing
- ✅ **Integration Tests**: 12/12 passing
- ✅ **Total Coverage**: Available in JaCoCo report

### Test Categories
1. **Model Tests** - Entity validation and business logic
2. **Service Tests** - Business logic and data operations
3. **Integration Tests** - HTTP endpoints and web layer
4. **Application Tests** - Spring context loading

## 🚀 Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Production Build
```bash
mvn clean package
java -jar target/task-manager-1.0.0.jar
```

### Docker Deployment
```bash
docker build -t task-manager .
docker run -d -p 8080:8080 --name task-manager-app task-manager
```

## 🔍 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **Database connection issues**
   ```bash
   # Check H2 console at http://localhost:8080/h2-console
   # JDBC URL: jdbc:h2:file:./taskdb
   # Username: sa
   # Password: password
   ```

3. **Test failures**
   ```bash
   # Run tests with verbose output
   mvn test -X
   
   # Check test reports
   open target/surefire-reports/
   ```

### Logs
- Application logs: Console output
- Test logs: `target/surefire-reports/`
- Coverage reports: `target/site/jacoco/`

## 📝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests: `mvn verify`
5. Run quality checks: `mvn spotbugs:check checkstyle:check pmd:check`
6. Commit and push
7. Create a pull request

The CI/CD pipeline will automatically run all checks on your PR.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
