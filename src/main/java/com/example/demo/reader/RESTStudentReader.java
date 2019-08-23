package com.example.demo.reader;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.StudentDTO;

public class RESTStudentReader implements ItemReader<StudentDTO>{

	private final String apiUrl;
    private final RestTemplate restTemplate;
 
    private int nextStudentIndex;
    private List<StudentDTO> studentData;
    
    public RESTStudentReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        nextStudentIndex = 0;
    }
 
    @Override
    public StudentDTO read() throws Exception {
        if (studentDataIsNotInitialized()) {
            studentData = fetchStudentDataFromAPI();
        }
 
        StudentDTO nextStudent = null;
 
        if (nextStudentIndex < studentData.size()) {
            nextStudent = studentData.get(nextStudentIndex);
            nextStudentIndex++;
        }
 
        return nextStudent;
    }
 
    private boolean studentDataIsNotInitialized() {
        return this.studentData == null;
    }
 
    private List<StudentDTO> fetchStudentDataFromAPI() {
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(
            apiUrl, 
            StudentDTO[].class
        );
        StudentDTO[] studentData = response.getBody();
        return Arrays.asList(studentData);
    }

}
