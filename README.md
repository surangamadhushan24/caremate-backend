# CareMate Backend

A comprehensive Spring Boot-based backend application for managing e-channeling services, providing a complete healthcare appointment and management system.

## Features

### User Management
- Secure JWT-based authentication and authorization
- Role-based access control (Admin, Doctor, Patient)
- User profile management and password updates

### Appointment Management
- Online appointment booking and scheduling
- Appointment status tracking and history
- Doctor availability management

### Healthcare Professionals
- Doctor profile management
- Specialization-based doctor search
- Patient management for doctors

### Communication & Feedback
- Real-time notifications system
- Feedback collection and management
- Integrated chatbot for user assistance

### Payment Integration
- Secure online payment processing
- Payment history and transaction management
- Integration with PayHere payment gateway

### Medical Records
- Digital prescription management
- Prescription access for patients
- Medical history tracking

### Telemedicine
- Video consultation scheduling
- Virtual meeting integration

### Analytics Dashboard
- Administrative reporting and analytics
- System usage statistics
- Performance monitoring

## Technology Stack
- **Java 17+**
- **Spring Boot** - Framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Database
- **JWT** - Token-based security
- **Maven** - Build tool
- **RESTful APIs** - Communication

## Project Architecture
```
src/
├── main/
│   ├── java/com/nibm/echannelling/echannelingapplication/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST API endpoints
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Global exception handling
│   │   ├── repository/     # Data access layer
│   │   ├── security/       # Security configurations
│   │   ├── service/        # Business logic
│   │   └── util/           # Utility classes
│   └── resources/
│       ├── application.yml      # Main configuration
│       └── application-local.yml # Local environment config
└── test/
    └── java/com/nibm/echannelling/echannelingapplication/
        └── EchannelingApplicationTests.java
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL database

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/surangamadhushan24/caremate-backend.git
   cd caremate-backend
   ```

2. **Configure Database**
   - Create a PostgreSQL database named `echannellingdb`
   - Update database credentials in `src/main/resources/application-local.yml`

3. **Build the Application**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**
   - API endpoints available at: `http://localhost:8080`
   - Swagger documentation (if configured) for API testing

## Configuration

### Environment Variables
The application uses environment variables for sensitive configuration:
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing secret
- `PAYHERE_MERCHANT_ID` - PayHere merchant ID
- `PAYHERE_MERCHANT_SECRET` - PayHere merchant secret
- `OPENROUTER_API_KEY` - OpenRouter API key

### Local Development
- Copy `application-local.yml` and configure local settings
- This file is gitignored for security

## API Documentation

The application provides RESTful APIs for:
- User authentication and management
- Appointment scheduling
- Doctor and patient management
- Payment processing
- Notification services
- Analytics and reporting

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Support

For questions or support, please open an issue in the GitHub repository.
