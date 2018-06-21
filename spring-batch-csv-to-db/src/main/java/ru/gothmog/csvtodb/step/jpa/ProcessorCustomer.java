package ru.gothmog.csvtodb.step.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import ru.gothmog.csvtodb.model.Customer;

import java.util.Random;

public class ProcessorCustomer implements ItemProcessor<Customer, Customer> {

    private static final Logger log = LoggerFactory.getLogger(ProcessorCustomer.class);

    @Override
    public Customer process(Customer customer) throws Exception {
        Random r = new Random();

        final String firstName = customer.getFirstName().toUpperCase();
        final String lastName = customer.getLastName().toUpperCase();

        final Customer fixedCustomer = new Customer(r.nextInt(), firstName, lastName);

        log.info("Converting (" + customer + ") into (" + fixedCustomer + " )");

        return fixedCustomer;
    }
}
