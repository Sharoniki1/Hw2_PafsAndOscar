package com.example.hw1application;

public class Paf implements GameObject {

    private int location;

    public Paf(){}

    public int getLocation() {
        return location;
    }

    public Paf setLocation(int location) {
        this.location = location;
        return this;
    }
}
