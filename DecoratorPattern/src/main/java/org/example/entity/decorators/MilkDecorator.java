package org.example.entity.decorators;

import org.example.entity.Coffee;

public class MilkDecorator extends   CoffeeDecorator{

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + " + milk(10rs)";
    }

    @Override
    public double getPrice() {
        return coffee.getPrice() + 10.0;
    }
}
