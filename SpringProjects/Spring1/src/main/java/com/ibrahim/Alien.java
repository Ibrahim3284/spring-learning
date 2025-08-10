package com.ibrahim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.beans.ConstructorProperties;

@Component
public class Alien {

    @Value("21")
    int age;

    @Autowired
//    @Qualifier("laptop")
    Computer com;
    int salary;

    public Alien() {
        System.out.println("Object created");
    }

//    @ConstructorProperties({"age", "lap", "salary"})
//    public Alien(int age, Laptop lap, int salary) {
//        System.out.println("Param constructor called");
//        this.age = age;
//        this.lap = lap;
//        this.salary = salary;
//    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public Computer getCom() {
        return com;
    }

    public void setCom(Computer com) {
        this.com = com;
    }

    public void setAge(int age) {
        System.out.println("Setter called");
        this.age = age;
    }

    public void code() {
        System.out.println("Coding");
        com.compile();
    }
}
