package com.ibrahim;

import org.springframework.stereotype.Component;

@Component
public class Laptop implements Computer{

    public Laptop() {
        System.out.println("Created laptop object");
    }

    @Override
    public void compile() {
        System.out.println("Compiling using laptop");
    }
}
