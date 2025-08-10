package com.ibrahim.spring_data_jpa_ex;

import com.ibrahim.spring_data_jpa_ex.model.Student;
import com.ibrahim.spring_data_jpa_ex.repo.StudentRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringDataJpaExApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringDataJpaExApplication.class, args);
		Student s1 = context.getBean(Student.class);
		Student s2 = context.getBean(Student.class);
		Student s3 = context.getBean(Student.class);

		StudentRepo repo = context.getBean(StudentRepo.class);
		s1.setRollNo(101);
		s1.setName("Ibrahim");
		s1.setMarks(48);

		s2.setRollNo(102);
		s2.setName("Mohammed");
		s2.setMarks(49);

		s3.setRollNo(103);
		s3.setName("Wajid");
		s3.setMarks(51);

		repo.save(s1);  // Also works on update
		repo.save(s2);
		repo.save(s3);

		repo.delete(s2);

		System.out.println(repo.findAll());
		System.out.println(repo.findById(102)); // By primary key
		System.out.println(repo.findByName("Ibrahim")); // By non primary key
	}

}
