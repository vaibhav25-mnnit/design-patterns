package observable;

import observer.NotificationAlertObserver;

public interface OutOfStockObservable {
    void add(NotificationAlertObserver observer);

    void remove(NotificationAlertObserver observer);

    void notifyObserver();

    int getData();

    void setData(int data);
}