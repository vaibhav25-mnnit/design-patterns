package observable;

import observer.NotificationAlertObserver;

import java.util.ArrayList;
import java.util.List;

public class OutOfStockObservableImpl implements OutOfStockObservable {
    int stock = 0;

    List<NotificationAlertObserver> observers = new ArrayList<>();


    @Override
    public void add(NotificationAlertObserver obj) {
        observers.add(obj);
    }

    @Override
    public void remove(NotificationAlertObserver obj) {
        observers.remove(obj);
    }

    @Override
    public void notifyObserver() {
        for(NotificationAlertObserver o : observers)  o.update();
    }

    @Override
    public int getData() {
        return stock;
    }

    @Override
    public void setData(int stock) {
        if(stock == 0)
        {
            notifyObserver();
        }
        this.stock = this.stock + stock;
    }
}
