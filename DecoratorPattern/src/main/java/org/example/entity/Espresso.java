package org.example.entity;

public class Espresso implements Coffee{
    @Override
    public String getDescription() {
        return "This is a espresso (50rs)";
    }

    @Override
    public double getPrice() {
        return 50.0;
    }
}
