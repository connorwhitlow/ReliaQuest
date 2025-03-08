Employee Management API
====================

Project Overview
---------------
A Spring Boot-based RESTful API for employee management with JWT authentication and rate limiting capabilities. This project demonstrates secure API development practices, including authentication, authorization, and rate limiting.

Technical Stack
--------------
- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Gradle
- PostgreSQL/H2 Database
- Rate Limiting Implementation

Security Features
----------------
1. JWT Authentication
   - Token-based authentication system
   - 24-hour token expiration
   - Secure secret key implementation

2. Rate Limiting
   - Authentication attempts: 5 requests per minute
   - API calls: 30 requests per minute per IP
   - Automatic cleanup mechanism

API Endpoints
------------
1. Authentication
   - POST /api/v1/auth/login
     * Credentials required: username, password
     * Returns: JWT token

2. Employee Management
   - GET    /api/v1/employee         (List all employees)
   - POST   /api/v1/employee         (Create new employee)
   - GET    /api/v1/employee/{id}    (Get single employee)
   - PUT    /api/v1/employee/{id}    (Update employee)
   - DELETE /api/v1/employee/{id}    (Delete employee)
   - GET    /api/v1/employee/search  (Search employees)

Setup Instructions
----------------
1. Environment Configuration
   - Application properties in application.yml
   - Default admin credentials:
     * Username: EmployeesRUS
     * Password: [Secure Password]

2. Database Setup
   - Configure database connection in application.yml
   - Tables are automatically created on startup

3. Running the Application
   ```bash
   ./gradlew bootRun
   ```

Testing
-------
1. Automated Testing Script
   - Location: ./test_api.sh
   - Makes script executable:
     ```bash
     chmod +x test_api.sh
     ```
   - Run tests:
     ```bash
     ./test_api.sh
     ```

2. Test Coverage
   - Authentication flow
   - CRUD operations
   - Rate limiting
   - Error handling

Security Considerations
---------------------
1. JWT Token
   - Secure storage of secret key
   - Token expiration handling
   - Token refresh mechanism

2. Rate Limiting
   - IP-based tracking
   - Separate limits for auth and API
   - Memory leak prevention

3. Data Validation
   - Input sanitization
   - Error handling
   - Proper HTTP status codes

Future Enhancements
-----------------
1. Potential Improvements
   - Token refresh endpoint
   - Role-based access control
   - Enhanced rate limiting options
   - Audit logging

2. Scalability Considerations
   - Database optimization
   - Caching implementation
   - Load balancing support

Troubleshooting
--------------
1. Common Issues
   - Rate limit exceeded
   - Token expiration
   - Database connectivity

2. Solutions
   - Wait for rate limit reset
   - Re-authenticate for new token
   - Check database configuration

Contact
-------
Developer: Connor
Project: Entry Level Java Challenge
Date: March 2024 