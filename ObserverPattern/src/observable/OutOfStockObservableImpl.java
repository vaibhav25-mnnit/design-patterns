package observable;

import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class OutOfStockObservableImpl implements Observable{
    int stock;

    List<Observer> observers = new ArrayList<>();


    @Override
    public void add(Observer obj) {
        observers.add(obj);
    }

    @Override
    public void remove(Observer obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObserver() {
        for(Observer o : observers)  o.update();
    }

    @Override
    public int getData() {
        return stock;
    }

    @Override
    public void setData(int stock) {
        this.stock = stock;
        notifyObserver();
    }
}
