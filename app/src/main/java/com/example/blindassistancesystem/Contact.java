package com.example.blindassistancesystem;

public class Contact {

    String name;
    String number;

    public Contact() {

    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }


    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
