package com.example.demo.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.dto.StudentDTO;

public class RESTStudentProcessor implements ItemProcessor<StudentDTO, StudentDTO>{

	@Override
	public StudentDTO process(StudentDTO item) throws Exception {
		
		return item;
	}

}
