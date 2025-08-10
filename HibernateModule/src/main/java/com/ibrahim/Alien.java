package com.ibrahim;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Alien {

    @Id
    private int aid;
    private String aname;
    private String tech;

    @ManyToMany
//    @ManyToMany(fetch = FetchType.EAGER)
    private List<Laptop> laptops;

    public Alien() {
    }

    public Alien(int aid, String aname, String tech, List<Laptop> laptops){
        this.aid = aid;
        this.aname = aname;
        this.tech = tech;
        this.laptops = laptops;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public List<Laptop> getLaptops() {
        return laptops;
    }

    public void setLaptops(List<Laptop> laptops) {
        this.laptops = laptops;
    }

//    @Override
//    public String toString() {
//        return "Alien{" +
//                "aid=" + aid +
//                ", aname='" + aname + '\'' +
//                ", tech='" + tech + '\'' +
//                ", laptops=" + laptops +
//                '}';
//    }
}
