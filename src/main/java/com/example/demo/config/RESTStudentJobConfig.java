package com.example.demo.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.StudentDTO;
import com.example.demo.listener.JobCompletionNotificationListener;
import com.example.demo.processor.RESTStudentProcessor;
import com.example.demo.reader.RESTStudentReader;

@Configuration
@EnableBatchProcessing
public class RESTStudentJobConfig {
	
	
 	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    private RestTemplate restTemplate;
	
    private Environment environment;
    
	@Bean
	ItemReader<StudentDTO> reader(Environment environment, RestTemplate restTemplate) {

		ItemReader<StudentDTO> obj = new RESTStudentReader(
				environment.getRequiredProperty("rest.api.to.database.job.api.url"), restTemplate);
		return obj;
	}
	 
	@Bean
	public RESTStudentProcessor processor() {
		return new RESTStudentProcessor();
	}

	@Bean
	public ItemWriter<StudentDTO> writer() {
		ItemWriter<StudentDTO> writer = new ItemWriter<StudentDTO>() {

			@Override
			public void write(List<? extends StudentDTO> items) throws Exception {

			}

		};
		return writer;
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer())
	                .listener(listener)
				.flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<StudentDTO, StudentDTO>chunk(10)
				.reader(reader(environment, restTemplate)).processor(processor()).writer(writer()).build();
	}

}
