package com.coditas.inmemorycache.repository;

import com.coditas.inmemorycache.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
