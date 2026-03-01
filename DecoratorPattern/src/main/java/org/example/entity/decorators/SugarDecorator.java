package org.example.entity.decorators;

import org.example.entity.Coffee;

public class SugarDecorator  extends  CoffeeDecorator{
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription()  + " + sugar(5rs)";
    }


    @Override
    public double getPrice() {
        return coffee.getPrice() + 5.0;
    }
}
