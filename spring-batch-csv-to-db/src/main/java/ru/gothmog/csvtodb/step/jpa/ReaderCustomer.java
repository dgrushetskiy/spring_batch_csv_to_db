package ru.gothmog.csvtodb.step.jpa;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import ru.gothmog.csvtodb.model.Customer;

public class ReaderCustomer {
    public static FlatFileItemReader<Customer> reader(String path) {

        FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();

        // claspath reading
        // reader.setResource(new ClassPathResource(path));
        // external filepath reading
        reader.setResource(new FileSystemResource(path));
        reader.setLineMapper(new DefaultLineMapper<Customer>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "id", "firstName", "lastName" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {
                    {
                        setTargetType(Customer.class);
                    }
                });
            }
        });
        return reader;
    }
}
