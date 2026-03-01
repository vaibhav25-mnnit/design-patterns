package org.example.entity;

public class FilterCoffee implements  Coffee{
    @Override
    public String getDescription() {
        return "This is a indian style filter coffee (30rs)";
    }

    @Override
    public double getPrice() {
        return 30.0;
    }
}
