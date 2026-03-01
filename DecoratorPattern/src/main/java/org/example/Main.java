package org.example;

import org.example.entity.BlackCoffee;
import org.example.entity.Coffee;
import org.example.entity.Espresso;
import org.example.entity.FilterCoffee;
import org.example.entity.decorators.MilkDecorator;
import org.example.entity.decorators.SugarDecorator;

public class Main {
    public static void main(String[] args)
    {
        System.out.println("Hello world! \nThis is coffiee machine to implement decorator pattern.");

        //Black coffee with milk and sugar
        Coffee order1 = new BlackCoffee();
        order1 = new MilkDecorator(order1);
        order1 = new SugarDecorator(order1);
        System.out.println("Order1 :"+order1.getDescription());
        System.out.println("Price :"+order1.getPrice());

        //Indian style filter coffee with sugar
        Coffee order2 = new FilterCoffee();
        order2 = new SugarDecorator(order2);
        System.out.println("Order2 :"+order2.getDescription());
        System.out.println("Price :"+order2.getPrice());


        //Plain Espresso with no addons
        Coffee order3 = new Espresso();
        System.out.println("Order2 :"+order3.getDescription());
        System.out.println("Price :"+order3.getPrice());

    }
}