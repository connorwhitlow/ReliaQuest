package com.challenge.api.request;

import java.time.Instant;

public class EmployeeRequest {
    private String firstName;

    private String lastName;

    private Integer salary;

    private String jobTitle;

    private String email;

    private Instant contractHireDate;

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getContractHireDate() {
        return contractHireDate;
    }

    public void setContractHireDate(Instant contractHireDate) {
        this.contractHireDate = contractHireDate;
    }
}
