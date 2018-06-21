package ru.gothmog.csvtodb.dao;

import ru.gothmog.csvtodb.model.Employee;

import java.util.List;

public interface EmployeeDao {

    void insert(List<? extends Employee> employees);

    List<Employee> loadAllEmployees();
}
