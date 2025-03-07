package com.challenge.api.service;

import com.challenge.api.model.DefaultEmployee;
import com.challenge.api.model.Employee;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private static HashMap<UUID, Employee> employeeMap;

    @PostConstruct
    public void createEmployees() {
        employeeMap = new HashMap<>();
        Employee e1 = new DefaultEmployee("Anthony", "Apicella", 100000, "Manager", "anthonyap@gmail.com");
        Employee e2 = new DefaultEmployee("Charles", "Wattick", 90000, "Salesman", "charleswa@gmail.com");
        Employee e3 = new DefaultEmployee("Benjamin", "Mapp", 80000, "Software Engineer", "benjaminma@gmail.com");
        Employee e4 = new DefaultEmployee("Aidan", "Fecteau", 200000, "CEO", "aidanfe@gmail.com");
        Employee e5 = new DefaultEmployee("Peter", "Chelap", 50000, "Accountant", "peterche@gmail.com");

        employeeMap.put(e1.getUuid(), e1);
        employeeMap.put(e2.getUuid(), e2);
        employeeMap.put(e3.getUuid(), e3);
        employeeMap.put(e4.getUuid(), e4);
        employeeMap.put(e5.getUuid(), e5);
        /*
        for(UUID u: employeeMap.keySet()) {
        	System.out.println(employeeMap.get(u).getFirstName());
        }
        */
    }

    /*
     * Returns all employees in map
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }

    /*
     * Returns employee with corresponding UUID
     */
    public Employee getEmployeeByUUID(UUID u) {
        return employeeMap.get(u);
    }

    /*
     *Creates an employee
     */
    public Employee createEmployee(Employee e) {
        if (e.getUuid() == null) {
            e.setUuid(UUID.randomUUID());
        }
        if (e.getContractHireDate() == null) {
            e.setContractHireDate(Instant.now());
        }
        employeeMap.put(e.getUuid(), e);
        return e;
    }
}
