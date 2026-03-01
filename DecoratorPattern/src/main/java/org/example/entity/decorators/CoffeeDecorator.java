package org.example.entity.decorators;

import org.example.entity.Coffee;

public abstract class CoffeeDecorator implements Coffee{

    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee){
        this.coffee = coffee;
    }


}
