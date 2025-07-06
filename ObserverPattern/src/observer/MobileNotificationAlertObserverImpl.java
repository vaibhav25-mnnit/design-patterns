package observer;

import observable.OutOfStockObservable;

public class MobileNotificationAlertObserverImpl implements NotificationAlertObserver{
    String userName;
    OutOfStockObservable observable;

    public MobileNotificationAlertObserverImpl(String userName,OutOfStockObservable observable){
        this.observable = observable;
        this.userName = userName;
    }

    @Override
    public void update() {
        sendMobileNotification(this.userName);
    }

    private  void sendMobileNotification(String userName)
    {
        System.out.println("Sending mobile notification to :- "+userName);
    }
}
