package com.ibrahim.SpringJDBCEx;

import com.ibrahim.SpringJDBCEx.model.Student;
import com.ibrahim.SpringJDBCEx.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringJdbcExApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringJdbcExApplication.class, args);

		Student s = context.getBean(Student.class);
		s.setRollNo(105);
		s.setName("Yahya");
		s.setMarks(98);

		StudentService studentService = context.getBean(StudentService.class);
		studentService.addStudent(s);
		System.out.println(studentService.getStudents());
	}

}
