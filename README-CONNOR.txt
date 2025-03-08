Security Features
----------------
1. JWT Authentication
   - Token-based authentication system
   - 24-hour token expiration
   - Secret key and password stored in .yml so you guys can test

2. Rate Limiting
   - Authentication attempts: 5 requests per minute
   - API calls: 30 requests per minute, per IP (Depending on scale, this could easily be adjusted in Line 25 of RateLimitingFilter.java)
   - Cleans up after 1 minute

3. Input Validation
   - Uses regex matching to validate inputs, require fields indicated were required
   - Dependencies were causing issues, manually implemented existing SpringBoot features like @NotNull 


API Endpoints
------------
1. Authentication
   - POST /api/v1/auth/login
     * Credentials required: username, password (located in yml file)
     * Returns: JWT token 

2. Employee Management
   - GET    /api/v1/employee         (List all employees)
   - POST   /api/v1/employee         (Create new employee)
   - GET    /api/v1/employee/{id}    (Get single employee)