package com.ibrahim;

import org.springframework.stereotype.Component;

@Component
public class Desktop implements Computer{

    public Desktop() {
        System.out.println("Created desktop object");
    }

    @Override
    public void compile() {
        System.out.println("Compiling using desktop");
    }
}
