# Task Manager - Spring Boot Application

A simple, modern task management application built with Spring Boot, featuring a clean and intuitive user interface.

## Features

- ✅ **Create, Read, Update, Delete (CRUD)** tasks
- 🎯 **Task Status Management**: Pending, In Progress, Completed, Cancelled
- ⚡ **Priority Levels**: Low, Medium, High, Urgent
- 📅 **Due Date Support** with overdue task alerts
- 🔍 **Filtering**: Filter tasks by status or priority
- 📱 **Responsive Design**: Works on desktop and mobile devices
- 🎨 **Modern UI**: Clean, intuitive interface with Bootstrap 5
- 💾 **Local Storage**: H2 database with file persistence
- 🔒 **Secure**: No external data access, runs completely locally

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Installation & Setup

1. **Clone or download** the project to your local machine
2. **Navigate** to the project directory:
   ```bash
   cd task-manager
   ```

3. **Build the application**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**:
   - Open your web browser
   - Go to: `http://localhost:8080`

## Usage

### Creating a Task
1. Fill in the task title (required)
2. Add an optional description
3. Select priority level
4. Set an optional due date
5. Click "Create Task"

### Managing Tasks
- **Edit**: Click the edit button (pencil icon) to modify task details
- **Delete**: Click the delete button (trash icon) to remove a task
- **Filter**: Use the filter dropdown to view tasks by status or priority

### Task Status
- **Pending**: New tasks awaiting action
- **In Progress**: Tasks currently being worked on
- **Completed**: Finished tasks
- **Cancelled**: Tasks that are no longer needed

### Priority Levels
- **Low**: Not urgent, can be done later
- **Medium**: Normal priority
- **High**: Important, should be done soon
- **Urgent**: Critical, needs immediate attention

## Database

The application uses H2 database with file persistence:
- Database file: `taskdb.mv.db` (created automatically)
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:./taskdb`
  - Username: `sa`
  - Password: `password`

## Security Features

- ✅ **No external data access**: All data is stored locally
- ✅ **No network requests**: Application runs completely offline
- ✅ **Input validation**: Server-side validation for all inputs
- ✅ **SQL injection protection**: Uses JPA/Hibernate with parameterized queries
- ✅ **XSS protection**: Thymeleaf template engine provides automatic escaping

## File Structure

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
│   │           ├── css/
│   │           │   └── style.css
│   │           └── js/
│   │               └── app.js
├── pom.xml
└── README.md
```

## Customization

### Changing the Port
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

### Changing the Database
The application can be easily modified to use other databases like MySQL, PostgreSQL, etc.

### Styling
- CSS styles are in `src/main/resources/static/css/style.css`
- Uses Bootstrap 5 for responsive design
- Custom animations and hover effects included

## Troubleshooting

### Port Already in Use
If port 8080 is already in use, change the port in `application.properties`:
```properties
server.port=8081
```

### Java Version Issues
Ensure you have Java 17 or higher:
```bash
java -version
```

### Maven Issues
If Maven is not installed, you can download it from: https://maven.apache.org/download.cgi

## Development

### Adding New Features
1. Add new fields to the `Task` entity
2. Update the repository with new query methods
3. Add business logic to the service layer
4. Update the controller with new endpoints
5. Modify the HTML template and add JavaScript if needed

### Running Tests
```bash
mvn test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For issues or questions, please check the troubleshooting section above or create an issue in the project repository.

---

**Enjoy managing your tasks efficiently! 🚀**
