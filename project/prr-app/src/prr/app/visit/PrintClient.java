package prr.app.visit;

import prr.Client;
import prr.visitors.ClientVisitor;

import static java.lang.Math.round;


public class PrintClient implements ClientVisitor {

    private char _dash = '|';

    private String _line = null;

    private String _notifications = "";

    private boolean _printSingleClient;

    public PrintClient(boolean singleClient){
        _printSingleClient = singleClient;
    }

    @Override
    public void visit(Client client){

        _line = "CLIENT" + _dash + client.getId() + _dash + client.getName() + _dash + client.getNif() + _dash +
                client.getLevelName() + _dash + (client.getNotificationState() ? "YES" : "NO") + _dash +
                client.getTerminals().size() + _dash + round(client.getPayments()) + _dash + round(client.getDebt()) +
                (_printSingleClient ? notifications(client) : "");

    }


    private String notifications (Client client) {

        _notifications = "";

        client.showNotifications().stream().forEach(notification -> _notifications += notification.getType() +
                _dash + notification.getOriginTerminal() + "\n" );

        return (_notifications != "" ? ("\n" +_notifications.substring(0, _notifications.length() -1)) : _notifications);

    }

    @Override
    public String getLine(){
        return _line;
    }

}
