package com.comp.ninti.general;


import java.io.Serializable;

public class Customer implements Serializable {

    private String name;
    private int age;
    private String email;
    private String phone;
    private long id;

    public Customer(String name) {
        this.name = name;
        age = 14;
        email = "example.name@provider.x";
        phone = "0175/12509122";
    }

    public Customer(String name, int age, String email, String phone) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public Customer(String name, int age, String email, String phone, long id) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
