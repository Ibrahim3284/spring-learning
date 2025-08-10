package com.ibrahim;

import org.springframework.stereotype.Component;

import java.beans.ConstructorProperties;

@Component
public class Student {

    int rollNo;
    String name;
    Computer comp;

    @ConstructorProperties({"rollNo", "name", "comp"})
    public Student(int rollNo, String name, Computer comp) {
        this.rollNo = rollNo;
        this.name = name;
        this.comp = comp;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Computer getComp() {
        return comp;
    }

    public void setComp(Computer comp) {
        this.comp = comp;
    }
}
