package com.ibrahim;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {
    public static void main(String[] args) {

//        Student s1 = new Student();
//        s1.setsName("Yahya");
//        s1.setsAge(19);
//        s1.setRollNo(6);

        Laptop l1 = new Laptop();
        l1.setLid(1);
        l1.setBrand("Asus");
        l1.setModel("Rog");
        l1.setRam(16);

        Alien a1 = new Alien();
        a1.setAid(101);
        a1.setAname("Raju");
        a1.setTech("Java");
        a1.setLaptops(List.of(l1));
        l1.setAlien(a1);

//        Configuration cfg = new Configuration();
//        cfg.addAnnotatedClass(com.ibrahim.Alien.class)
//                .addAnnotatedClass(com.ibrahim.Laptop.class);
//        cfg.configure();

//        SessionFactory sf = cfg.buildSessionFactory();
        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(com.ibrahim.Alien.class)
                .addAnnotatedClass(com.ibrahim.Laptop.class)
                .configure()
                .buildSessionFactory();
        Session session = sessionFactory.openSession();


        Transaction transaction = session.beginTransaction();
        session.persist(l1);
        session.persist(a1);
        transaction.commit();
        session.close();
        sessionFactory.close();

        // Fetching data
//        Student s2 = null;
//        s2 = session.find(Student.class, 2);

        // Updating data
//        Transaction transaction = session.beginTransaction();
//        session.merge(s1);
//        transaction.commit();

        // Delete data
//        s1 = session.find(Student.class, 3);
//        Transaction transaction = session.beginTransaction();
//        session.remove(s1);
//        transaction.commit();
//
//        System.out.println(s1);
    }
}