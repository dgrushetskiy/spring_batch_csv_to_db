package ru.gothmog.csvtodb.step.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import ru.gothmog.csvtodb.dao.EmployeeDao;
import ru.gothmog.csvtodb.model.Employee;

import java.util.List;

public class ListenerEmployee extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(ListenerEmployee.class);

    private final EmployeeDao employeeDao;

    public ListenerEmployee(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            log.info("Finish Job! Check the results");

            List<Employee> employees = employeeDao.loadAllEmployees();

            for (Employee employee : employees) {
                log.info("Found <" + employee + "> in the database.");
            }
        }
    }
}
