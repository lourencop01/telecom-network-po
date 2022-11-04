package prr.delivery;

import prr.notifications.Notification;

import java.io.Serializable;
import java.util.Collection;

public abstract class DeliveryMethod implements Serializable {

    public abstract void addNotificationsMethod(Notification notification);

    public abstract Collection<Notification> showNotificationsMethod();

}
