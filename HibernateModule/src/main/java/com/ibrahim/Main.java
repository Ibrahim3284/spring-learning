package com.ibrahim;


import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {
    public static void main(String[] args) {


        Laptop l1 = new Laptop();
        l1.setLid(1);
        l1.setModel("Asus");
        l1.setRam(16);

        Laptop l2 = new Laptop();
        l2.setLid(2);
        l2.setModel("Dell");
        l2.setRam(8);

        Laptop l3 = new Laptop();
        l3.setLid(3);
        l3.setModel("Lenovo");
        l3.setRam(24);

        Alien a1 = new Alien(1, "Alien1", "Java", List.of(l1, l3));
        Alien a2 = new Alien(2, "Alien2", "C++", List.of(l1));
        Alien a3 = new Alien(3, "Alien3", "Python", List.of(l2, l3));

        l1.setAliens(List.of(a1, a2));
        l2.setAliens(List.of(a3));
        l3.setAliens(List.of(a1, a3));

        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(com.ibrahim.Alien.class)
                .addAnnotatedClass(com.ibrahim.Laptop.class)
                .configure()
                .buildSessionFactory();

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        session.persist(l1);
        session.persist(l2);
        session.persist(l3);

        session.persist(a1);
        session.persist(a2);
        session.persist(a3);

        transaction.commit();

        session.close();

        Session session1 = sessionFactory.openSession();
        //        Laptop laptop = session.find(Laptop.class, 3);
//        System.out.println(laptop);

        // SELECT * FROM laptop where ram = 32 -> SQL
        // FROM laptop where ram = 32; -> HQL

        String brand = "Dell";

        Query query = session1.createQuery("from Laptop WHERE model like ?1");
        query.setParameter(1, brand);
        List<Laptop> laptops = query.getResultList();
        System.out.println(laptops);

        session1.close();
        sessionFactory.close();
    }
}