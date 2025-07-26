import observable.OutOfStockObservable;
import observable.OutOfStockObservableImpl;
import observer.EmailNotificationAlertObserverImpl;
import observer.MobileNotificationAlertObserverImpl;
import observer.NotificationAlertObserver;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        OutOfStockObservable observable = new OutOfStockObservableImpl();


        NotificationAlertObserver observer1 = new EmailNotificationAlertObserverImpl("vaibhav",observable);

        NotificationAlertObserver observer2 = new MobileNotificationAlertObserverImpl("vaibhav",observable);

        NotificationAlertObserver observer3 = new MobileNotificationAlertObserverImpl("sumit",observable);

        observable.add(observer1);
        observable.add(observer2);
        observable.add(observer3);

        observable.setData(10);
        observable.setData(0);


    }
}