package ru.gothmog.csvtodb.step.jdbc;

import org.springframework.batch.item.ItemWriter;
import ru.gothmog.csvtodb.dao.EmployeeDao;
import ru.gothmog.csvtodb.model.Employee;

import java.util.List;

public class WriterEmployee implements ItemWriter<Employee> {
    private final EmployeeDao employeeDao;

    public WriterEmployee(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public void write(List<? extends Employee> employees) throws Exception {
        employeeDao.insert(employees);
    }
}
