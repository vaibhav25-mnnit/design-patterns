package org.example.entity;

public class BlackCoffee implements  Coffee {
    @Override
    public String getDescription() {
        return "This is a black coffee(25rs)";
    }

    @Override
    public double getPrice() {
        return 25.0;
    }
}
