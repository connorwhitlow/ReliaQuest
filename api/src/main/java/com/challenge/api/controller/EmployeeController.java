package com.challenge.api.controller;

import com.challenge.api.model.DefaultEmployee;
import com.challenge.api.model.Employee;
import com.challenge.api.request.EmployeeRequest;
import com.challenge.api.service.EmployeeService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Employee API Endpoints
 * 
 * Security Notes:
 * --------------
 * 1. Who Can Do What:
 *    - Anyone can view the list of employees
 *    - Only admins can create new employees
 *    - Check if user is logged in before allowing any actions
 * 
 * 2. Keeping Things Safe:
 *    - Make sure data is sent securely (use HTTPS)
 *    - Check that all input data is valid
 *    - Don't let users request too much data at once
 * 
 * 3. Good Practices:
 *    - Keep track of who's using the API
 *    - Don't show detailed error messages to users
 *    - Double check all employee IDs before using them
 */
@RestController
@RequestMapping("/api/v1/employee")
@Validated
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    // Regex patterns for validation
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s-']{2,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern JOB_TITLE_PATTERN = Pattern.compile("^[a-zA-Z\\s-']{2,100}$");
    private static final Pattern SALARY_PATTERN = Pattern.compile("^[0-9]{1,7}$");

    @GetMapping
    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee models as necessary.
     * @return One or more Employees.
     */
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{uuid}")
    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee model as necessary.
     * @param uuid Employee UUID
     * @return Requested Employee if exists
     */
    public Employee getEmployeeByUuid(@PathVariable("uuid") UUID uuid) {
        Employee employee = employeeService.getEmployeeByUUID(uuid);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        return employee;
    }

    @PostMapping
    /**
     * @implNote Need not be concerned with an actual persistence layer.
     * @param requestBody hint!
     * @return Newly created Employee
     */
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequest request) {
        // Validate the request first
        validateEmployeeRequest(request);

        // Only create employee if validation passes
        Employee employee = new DefaultEmployee(
                request.getFirstName().trim(),
                request.getLastName().trim(),
                request.getSalary(),
                request.getJobTitle().trim(),
                request.getEmail().trim().toLowerCase());

        // Set contract hire date if provided in request
        if (request.getContractHireDate() != null) {
            employee.setContractHireDate(request.getContractHireDate());
        }

        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    /*
     * Validates input for employee
     * I understand there are annotations to do this, however the dependencies were causing build errors
     * Drawback of this is that only the first caught error is reported back to the user
     */
    private void validateEmployeeRequest(EmployeeRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body cannot be null");
        }

        // Validate First Name
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            System.out.println("Hi!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (!NAME_PATTERN.matcher(request.getFirstName().trim()).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "First name must contain only letters, spaces, hyphens, and apostrophes (2-50 characters)");
        }

        // Validate Last Name
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        if (!NAME_PATTERN.matcher(request.getLastName().trim()).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Last name must contain only letters, spaces, hyphens, and apostrophes (2-50 characters)");
        }

        // Validate Email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (!EMAIL_PATTERN.matcher(request.getEmail().trim().toLowerCase()).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid email format. Must be in format: username@domain.com");
        }

        // Validate Salary
        if (request.getSalary() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary is required");
        }
        String salaryStr = String.valueOf(request.getSalary());
        if (!SALARY_PATTERN.matcher(salaryStr).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Salary must be a positive number with no decimal places");
        }
        if (request.getSalary() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary must be positive");
        }
        if (request.getSalary() > 1000000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary seems unreasonably high");
        }

        // Validate Job Title
        if (request.getJobTitle() == null || request.getJobTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job title is required");
        }
        if (!JOB_TITLE_PATTERN.matcher(request.getJobTitle().trim()).matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Job title must contain only letters, spaces, hyphens, and apostrophes (2-100 characters)");
        }

        // Validate Contract Hire Date
        if (request.getContractHireDate() != null) {
            Instant now = Instant.now();
            if (request.getContractHireDate().isAfter(now)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contract hire date cannot be in the future");
            }
        }
    }
}
