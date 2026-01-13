# Echanneling Application

This is a Spring Boot-based web application for managing e-channeling services, including appointments, doctors, patients, feedback, notifications, payments, prescriptions, and video calls.

## Features

### User Management
- Secure user authentication and authorization using JWT
- Role-based access for Admin, Doctor, and Patient
- Change password and profile management

### Appointment System
- Book, update, and cancel appointments
- View appointment history
- Doctor availability management

### Doctor & Patient Management
- Add, update, and delete doctor and patient profiles
- Search and filter doctors by specialization

### Feedback & Notification
- Submit and view feedback
- Automated notifications for appointments and updates

### Payment Processing
- Online payment integration for appointments
- Payment history and receipts

### Prescription Management
- Doctors can create and manage prescriptions
- Patients can view and download prescriptions

### Video Call Integration
- Schedule and join video consultations

### Analytics & Reporting
- Admin dashboard for analytics and reports

### RESTful API Endpoints
- Well-documented APIs for all major features

### Other Features
Chatbot integration for user queries

## Technologies Used
- Java 17+
- Spring Boot
- Maven
- Spring Security (JWT)
- Thymeleaf (for templates)
- REST APIs

## Project Structure
```
src/
  main/
    java/
      com/nibm/echannelling/echannelingapplication/
        config/
        controller/
        dto/
        entity/
        exception/
        repository/
        security/
        service/
        util/
    resources/
      application.yml
      static/
      templates/
  test/
    java/
      com/nibm/echannelling/echannelingapplication/
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Build and Run
1. Clone the repository:
   ```sh
   git clone <repo-url>
   ```
2. Navigate to the project directory:
   ```sh
   cd echanneling-application
   ```
3. Build the project:
   ```sh
   mvn clean install
   ```
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

### Access
- The application will be available at `http://localhost:8080`

## Configuration
- Main configuration file: `src/main/resources/application.yml`
- Update database and other service credentials as needed.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License.
