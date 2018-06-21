package ru.gothmog.csvtodb.step.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import ru.gothmog.csvtodb.model.Employee;

import java.util.Random;

public class ProcessorEmployee implements ItemProcessor<Employee, Employee> {
    private static final Logger log = LoggerFactory.getLogger(ProcessorEmployee.class);

    @Override
    public Employee process(Employee employee) throws Exception {
        Random r = new Random();

        final String firstName = employee.getFirstName().toUpperCase();
        final String lastName = employee.getLastName().toUpperCase();

        final Employee fixedEmployee = new Employee(r.nextInt(), firstName, lastName);

        log.info("Converting (" + employee + ") into (" + fixedEmployee + ") ");

        return fixedEmployee;
    }
}
