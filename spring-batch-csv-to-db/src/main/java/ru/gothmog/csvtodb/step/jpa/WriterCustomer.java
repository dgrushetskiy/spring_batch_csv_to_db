package ru.gothmog.csvtodb.step.jpa;

import org.springframework.batch.item.ItemWriter;
import ru.gothmog.csvtodb.model.Customer;
import ru.gothmog.csvtodb.repository.CustomerRepository;

import java.util.List;

public class WriterCustomer implements ItemWriter<Customer> {

    private final CustomerRepository customerRepository;

    public WriterCustomer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void write(List<? extends Customer> customers) throws Exception {
        customerRepository.insert(customers);
    }
}
