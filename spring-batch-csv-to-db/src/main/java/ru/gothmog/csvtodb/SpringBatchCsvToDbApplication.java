package ru.gothmog.csvtodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchCsvToDbApplication implements CommandLineRunner{

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job customerJob;

	@Autowired
	Job employeeJob;

	@Autowired
	Job customerJobReverse;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchCsvToDbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		try {

			/* Customer Job */
			JobParameters jobParameters1 = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(customerJob, jobParameters1);
			logger.info("Customer job complete...");
			/* Customer Job */

			/* Customer File Job */
			JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(customerJobReverse, jobParameters);
			logger.info("Customer File job complete...");
			/* Customer File Job */

			/* Employee Job */
			JobParameters jobParameters2 = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(employeeJob, jobParameters2);
			logger.info("Employee job complete...");
			/* Employee Job */

		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}
}
