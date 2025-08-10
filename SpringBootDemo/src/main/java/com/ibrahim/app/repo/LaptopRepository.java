package com.ibrahim.app.repo;

import com.ibrahim.app.model.Laptop;
import org.springframework.stereotype.Repository;

@Repository
public class LaptopRepository {

    public void save(Laptop lap) {
        System.out.println("Saved in database.");
    }
}
