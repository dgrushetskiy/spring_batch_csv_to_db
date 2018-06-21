package ru.gothmog.csvtodb.repository;

import ru.gothmog.csvtodb.model.Customer;

import java.util.List;

public interface CustomerRepository {

    List<Customer> findAll();

    void insert(List<? extends Customer> customers);
}
