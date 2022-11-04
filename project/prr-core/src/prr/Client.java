package prr;

import prr.communications.Communication;
import prr.delivery.DeliveryMethod;
import prr.exceptions.UnrecognizedEntryException;
import prr.levels.Level;
import prr.levels.Normal;
import prr.notifications.Notification;
import prr.terminals.Terminal;
import prr.visitors.ClientVisitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Client implements Serializable {

    private static final long serialVersionUID = 202216101522L;

    private final String _id;

    private String _name;

    private int _nif;

    private boolean _notificationState;

    private List<Notification> _notifications;

    DeliveryMethod _deliveryMethod = new Default();

    private Level _level;

    private Map<String, Terminal> _terminals;

    public Client(String id, String name, int nif){

        _id = id;
        _name = name;
        _nif = nif;

        _notificationState = true;
        _notifications = new ArrayList<>();
        _level = new Normal(this);
        _terminals = new TreeMap<>();


    }

    private class Default extends DeliveryMethod {

        @Override
        public void addNotificationsMethod(Notification notification) {
            if (getNotificationState()) {
                _notifications.add(notification);
            }
        }

        @Override
        public Collection<Notification> showNotificationsMethod() {
            List<Notification> localNotifications = new ArrayList<>(getNotifications());
            _notifications.clear();
            return Collections.unmodifiableCollection(localNotifications);
        }

    }

    public void addNotifications(Notification notification) {
        _deliveryMethod.addNotificationsMethod(notification);
    }

    public Collection<Notification> showNotifications() {
        return _deliveryMethod.showNotificationsMethod();
    }

    public Collection<Notification> getNotifications() {
        return Collections.unmodifiableCollection(_notifications); }

    public void putTerminal(Terminal terminal){
        _terminals.put(terminal.getId(), terminal);
    }

    public void shouldChangeLevelAfterCommunication() {
        if (!getLevelName().equals("PLATINUM")) {
            _level.shouldUpgrade();
        }
        if (!getLevelName().equals("NORMAL")) {
            _level.shouldDegrade();
        }
    }

    public void shouldChangeLevelAfterPayment() {
        if (getLevelName().equals("NORMAL")){
            _level.shouldUpgrade();
        }
    }

    public void accept(ClientVisitor visitor){
        visitor.visit(this);
    }

    public String getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public int getNif(){
        return _nif;
    }

    public double getPayments(){
        double payed = 0;
        for (Terminal terminal: _terminals.values()){
            payed += terminal.getPayments();
        }
        return payed;
    }

    public double getDebt(){
        double debt = 0;
        for (Terminal terminal: _terminals.values()){
            debt += terminal.getDebt();
        }
        return debt;
    }

    public double getBalance() {
        return getPayments() - getDebt();
    }

    public void setLevel(Level newLevel) {
        _level = newLevel;
    }

    public String getLevelName(){
        return _level.getLevelName();
    }

    public boolean getNotificationState(){
        return _notificationState;
    }

    public Collection<Terminal> getTerminals(){
            return Collections.unmodifiableCollection(_terminals.values());
    }

    public void setNotificationState(boolean turn){
        _notificationState = turn;
    }

    public Collection<Communication> sentCommunications(){

        List<Communication> sentComms = new ArrayList<>();

        _terminals.values().forEach(terminal -> terminal.getSentCommunications().stream()
                .filter(communication -> !sentComms.contains(communication))
                .forEach(communication -> sentComms.add(communication)));

        return Collections.unmodifiableCollection(sentComms);
    }

    public Collection<Communication> receivedCommunications(){

        List<Communication> receivedComms = new ArrayList<>();

        _terminals.values().forEach(terminal -> terminal.getReceivedCommunications().stream()
                .filter(communication -> !receivedComms.contains(communication))
                .forEach(communication -> receivedComms.add(communication)));

        return Collections.unmodifiableCollection(receivedComms);

    }

    public boolean lastXCommunicationsAreType(int number, String type) {

        List <Communication> comms = new ArrayList<>(sentCommunications());

        if (comms.size() >= number) {
            for (int i = comms.size() - 1; i >= comms.size() - number; i--) {
                if (!comms.get(i).getType().equals(type)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public double calculateCommunicationPrice(String type, double size, boolean areFriends) throws UnrecognizedEntryException {
            return _level.calculatePrice(type, size, areFriends);
    }

}
