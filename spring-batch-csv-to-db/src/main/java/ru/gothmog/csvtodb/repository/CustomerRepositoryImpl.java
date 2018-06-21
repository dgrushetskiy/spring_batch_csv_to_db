package ru.gothmog.csvtodb.repository;

import org.springframework.stereotype.Repository;
import ru.gothmog.csvtodb.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Customer> findAll() {
        return this.entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }

    @Override
    public void insert(List<? extends Customer> customers) {
        try {

            for (int i = 0; i < customers.size(); ++i) {
                if (i > 0 && i % customers.size() == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }

                Customer customer = customers.get(i);
                entityManager.persist(customer);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
