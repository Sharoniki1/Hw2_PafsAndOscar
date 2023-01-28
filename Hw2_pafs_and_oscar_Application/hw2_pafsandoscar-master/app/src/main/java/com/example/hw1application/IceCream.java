package com.example.hw1application;

public class IceCream implements GameObject {

    private int location;
    private final int BONUS = 5;

    public IceCream() {}

    public IceCream setLocation(int location) {
        this.location = location;
        return this;
    }

    public int getLocation() {
        return location;
    }

    public int getBONUS() {
        return BONUS;
    }
}
