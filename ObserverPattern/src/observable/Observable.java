package observable;

import observer.Observer;

public interface Observable {
    void add(Observer obj);

    void remove(Observer obj);

    void notifyObserver();

    int getData();

    void setData(int data);
}
