package ru.gothmog.csvtodb.step.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import ru.gothmog.csvtodb.model.Customer;
import ru.gothmog.csvtodb.repository.CustomerRepository;

import java.util.List;

public class ListenerCustomer extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(ListenerCustomer.class);

    private final CustomerRepository customerRepository;

    public ListenerCustomer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Starting Job!");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Finish Job! Check the results");

            List<Customer> customers = customerRepository.findAll();

            for (Customer customer : customers) {
                log.info("Found <" + customer + "> in the database.");
            }
        }
    }
}
