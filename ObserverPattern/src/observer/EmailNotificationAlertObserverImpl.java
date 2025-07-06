package observer;

import observable.OutOfStockObservable;

public class EmailNotificationAlertObserverImpl implements NotificationAlertObserver{

    String userName;
    OutOfStockObservable observable;

    public EmailNotificationAlertObserverImpl(String userName,OutOfStockObservable observable)
    {
        this.userName = userName;
        this.observable = observable;
    }


    @Override
    public void update() {
        sendEmail(this.userName);
    }

    private void sendEmail(String userName){
        System.out.println("Sending out of stock email to :- "+userName);
    }
}
