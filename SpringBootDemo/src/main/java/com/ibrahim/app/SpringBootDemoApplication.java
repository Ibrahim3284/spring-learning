package com.ibrahim.app;

import com.ibrahim.app.model.Alien;
import com.ibrahim.app.model.Laptop;
import com.ibrahim.app.service.LaptopService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringBootDemoApplication.class, args);
//		System.out.println("Spring Boot App running");

		// If we are managing objects
//		Alien obj = new Alien();
//		obj.code();

		LaptopService service = context.getBean(LaptopService.class);
		Laptop lap = context.getBean(Laptop.class);
		service.addLaptop(lap);

//		Alien obj1 = context.getBean(Alien.class);
//		obj1.code();
//		System.out.println(obj1.getAge());
	}

}
