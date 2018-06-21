package ru.gothmog.csvtodb.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import ru.gothmog.csvtodb.dao.EmployeeDao;
import ru.gothmog.csvtodb.model.Customer;
import ru.gothmog.csvtodb.model.Employee;
import ru.gothmog.csvtodb.repository.CustomerRepository;
import ru.gothmog.csvtodb.repository.JpaQueryProviderImpl;
import ru.gothmog.csvtodb.step.jdbc.ListenerEmployee;
import ru.gothmog.csvtodb.step.jdbc.ProcessorEmployee;
import ru.gothmog.csvtodb.step.jdbc.ReaderEmployee;
import ru.gothmog.csvtodb.step.jdbc.WriterEmployee;
import ru.gothmog.csvtodb.step.jpa.ListenerCustomer;
import ru.gothmog.csvtodb.step.jpa.ProcessorCustomer;
import ru.gothmog.csvtodb.step.jpa.ReaderCustomer;
import ru.gothmog.csvtodb.step.jpa.WriterCustomer;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private String filePath = "D:\\data\\";

    private String inputFile = filePath + "customer-data.csv";

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    public EmployeeDao employeeDao;

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Bean
    public Job customerJob(){
        return  jobBuilderFactory.get("customerJob").incrementer(new RunIdIncrementer())
                .listener(new ListenerCustomer(customerRepository)).flow(customerJobStep())
                .end().build();
    }

    @Bean
    public Step customerJobStep() {
        return stepBuilderFactory.get("customerJobStep").<Customer, Customer>chunk(2)
                .reader(ReaderCustomer.reader(inputFile))
                .processor(new ProcessorCustomer())
                .writer(new WriterCustomer(customerRepository)).build();
    }

    @Bean
    public Job employeeJob() {
        return jobBuilderFactory.get("employeeJob").incrementer(new RunIdIncrementer())
                .listener(new ListenerEmployee(employeeDao)).flow(employeeJobStep()).end()
                .build();
    }

    @Bean
    public Step employeeJobStep() {
        return stepBuilderFactory.get("employeeJobStep").<Employee, Employee>chunk(2)
                .reader(ReaderEmployee.reader(inputFile))
                .processor(new ProcessorEmployee())
                .writer(new WriterEmployee(employeeDao)).build();
    }

    @Bean
    public Job customerJobReverse()
            throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
        return jobBuilderFactory.get("customerJobReverse").incrementer(new RunIdIncrementer())
                .listener(new ListenerCustomer(customerRepository))
                .flow(customerJobReverseStep()).end().build();
    }

    @Bean
    public Step customerJobReverseStep()
            throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {

        return stepBuilderFactory.get("customerJobReverseStep").<Customer, Customer>chunk(2)
                .reader(reader())
                .processor(new ProcessorCustomer()).writer(writer()).build();
    }

    @Bean
    public JpaPagingItemReader<Customer> reader() throws Exception {
        JpaPagingItemReader<Customer> databaseReader = new JpaPagingItemReader<>();
        databaseReader.setEntityManagerFactory(entityManagerFactory);
        JpaQueryProviderImpl<Customer> jpaQueryProvider = new JpaQueryProviderImpl<>();
        jpaQueryProvider.setQuery("Customer.findAll");
        databaseReader.setQueryProvider(jpaQueryProvider);
        databaseReader.setPageSize(1000);
        databaseReader.afterPropertiesSet();
        return databaseReader;
    }

    @Bean
    public StaxEventItemWriter<Customer> writer() {
        StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<>();
        writer.setResource(new FileSystemResource(filePath+"\\customer.xml"));
        writer.setMarshaller(customerUnmarshaller());
        writer.setRootTagName("customers");
        return writer;
    }

    @Bean
    public XStreamMarshaller customerUnmarshaller() {
        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        @SuppressWarnings("rawtypes")
        Map<String, Class> aliases = new HashMap<String, Class>();
        aliases.put("customer", Customer.class);
        try {
            unMarshaller.setAliases(aliases);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return unMarshaller;
    }
}
