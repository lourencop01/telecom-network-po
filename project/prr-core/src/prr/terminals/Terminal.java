package prr.terminals;

import prr.Client;
import prr.Network;
import prr.communications.Communication;
import prr.communications.InteractiveCommunication;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.InvalidCommunicationException;
import prr.exceptions.NoOngoingCommunicationException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.alreadyexists.TerminalDoesNotExistException;
import prr.exceptions.alreadyisatstate.AlreadyIsOffException;
import prr.exceptions.alreadyisatstate.AlreadyIsOnException;
import prr.exceptions.alreadyisatstate.AlreadyIsSilentException;
import prr.exceptions.unsupported.UnsupportedDestinationTerminalException;
import prr.exceptions.unsupported.UnsupportedOriginTerminalException;
import prr.notifications.Notification;
import prr.states.State;
import prr.visitors.TerminalVisitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable {

    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = 202208091753L;

    private final String _id;

    private double _paid;

    private double _debt;

    private Client _owner;

    private Map<String, Terminal> _friends;

    private Map<Integer, Communication> _communications;

    private InteractiveCommunication _ongoingCommunication;

    private Communication _lastCommunication;

    private Map<Client, ArrayList<String>> _sendNotificationsTo;

    private State _state;

    private State _previousState;

    public Terminal(String id, Client owner) {

        _id = id;
        _owner = owner;

        _paid = 0;
        _debt = 0;
        _friends = new TreeMap<>();
        _communications = new TreeMap<>();
        _state = null;
        _previousState = null;
        _ongoingCommunication = null;
        _lastCommunication = null;
        _sendNotificationsTo = new LinkedHashMap<>();

    }

    /**
     * Checks if this terminal can end the current interactive communication.
     *
     * @return true if this terminal is busy (i.e., it has an active interactive communication) and
     * it was the originator of this communication.
     **/
    public boolean canEndCurrentCommunication() {
        return getStateName().equals("BUSY") && startedOngoingComm();
    }

    /**
     * Checks if this terminal can start a new communication.
     *
     * @return true if this terminal is neither off neither busy, false otherwise.
     **/
    public boolean canStartCommunication() {
        return !getStateName().equals("OFF") && !getStateName().equals("BUSY");
    }

    public void newTextMessage(String message, String to, Network network) throws TerminalDoesNotExistException,
            DestinationIsOffException {

        Terminal destinationTerminal = network.getTerminal(to);

        if (!isItself(destinationTerminal)) {

            checkDestinationOff(destinationTerminal, "TEXT");

            try {
                network.registerCommunication("TEXT", _id, to, message);
            } catch (UnrecognizedEntryException e) {
                e.printStackTrace();
            }

            _lastCommunication = network.getLastCommunication();

            calculateCommunicationPrice(_lastCommunication);
            addDebt(_lastCommunication.getPrice());

            _owner.shouldChangeLevelAfterCommunication();
        }
    }

    public void newInteractiveMessage(String type, String to, Network network) throws TerminalDoesNotExistException,
            DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException,
            UnsupportedOriginTerminalException, UnsupportedDestinationTerminalException {

        Terminal destinationTerminal = network.getTerminal(to);

        if (!isItself(destinationTerminal)) {

            checkDestinationOff(destinationTerminal, type);
            checkDestinationSilent(destinationTerminal, type);
            checkDestinationBusy(destinationTerminal, type);

            if (terminalIsBasic(this) && type.equals("VIDEO")) {
                throw new UnsupportedOriginTerminalException(_id, getType());
            } else if (terminalIsBasic(network.getTerminal(to)) && type.equals("VIDEO")) {
                throw new UnsupportedDestinationTerminalException(destinationTerminal.getId(), destinationTerminal.getType());
            }

            try {
                network.registerCommunication(type, _id, to);
            } catch (UnrecognizedEntryException e) {
                e.printStackTrace();
            }

            setOngoingCommunication((InteractiveCommunication) network.getLastCommunication());
            destinationTerminal.setOngoingCommunication((InteractiveCommunication) network.getLastCommunication());

            _lastCommunication = _ongoingCommunication;

            savePreviousState();
            destinationTerminal.savePreviousState();
            goToBusy();
            destinationTerminal.goToBusy();
        }
    }

    public void endCurrentCommunication(double minutes, Network network) {

        _ongoingCommunication.setUnits(minutes);
        calculateCommunicationPrice(_ongoingCommunication);
        addDebt(_ongoingCommunication.getPrice());

        _ongoingCommunication.setStatus(false);

        retrievePreviousState();
        try {
            network.getTerminal(_ongoingCommunication.toTerminalId()).retrievePreviousState();
        } catch (TerminalDoesNotExistException e) {
            e.printStackTrace();
        }

        _ongoingCommunication = null;

        _owner.shouldChangeLevelAfterCommunication();

    }

    public void checkDestinationOff(Terminal destinationTerminal, String commType) throws DestinationIsOffException {

        if (destinationTerminal.getStateName().equals("OFF")) {
            destinationTerminal.addNotificationToSend(getOwner(), commType);
            throw new DestinationIsOffException(destinationTerminal.getId());
        }

    }

    public void checkDestinationBusy(Terminal destinationTerminal, String commType) throws DestinationIsBusyException {

        if (destinationTerminal.getStateName().equals("BUSY")) {
            destinationTerminal.addNotificationToSend(getOwner(), commType);
            throw new DestinationIsBusyException(destinationTerminal.getId());
        }

    }

    public void checkDestinationSilent(Terminal destinationTerminal, String commType) throws DestinationIsSilentException {

        if (destinationTerminal.getStateName().equals("SILENCE")) {
            destinationTerminal.addNotificationToSend(getOwner(), commType);
            throw new DestinationIsSilentException(destinationTerminal.getId());
        }

    }

    public boolean terminalIsBasic(Terminal terminal) {
        return terminal.getType().equals("BASIC");
    }

    public boolean isItself(Terminal terminal) {
        return terminal.getId().equals(_id);
    }

    public void addFriend(String id, Network network) throws TerminalDoesNotExistException {

        if (!_friends.containsKey(id) && !_id.equals(id)) {
            _friends.put(id, network.getTerminal(id));
        }

    }

    public void removeFriend(String id, Network network) throws TerminalDoesNotExistException {

        if (_friends.containsKey(id)) {
            _friends.remove(id, network.getTerminal(id));
        }

    }

    public void performPayment(int id) throws InvalidCommunicationException {

        if (_communications.containsKey(id)) {
            Communication localComm = _communications.get(id);

            if (localComm.fromTerminalId().equals(_id) && !localComm.isPaid() && !localComm.getStatus()) {
                addDebt(localComm.getPrice() * (-1));
                addPayment(localComm.getPrice());
                localComm.setPaid();

                _owner.shouldChangeLevelAfterPayment();
            } else {
                throw new InvalidCommunicationException(id);
            }
        }
    }

    public void goToIdle() throws AlreadyIsOnException {
        if (!hasOngoingCommunication()) {
            if (!getStateName().equals("IDLE")) {
                _state.goToIdle();
            } else {
                throw new AlreadyIsOnException();
            }
        }
    }

    public void goToSilence() throws AlreadyIsSilentException {
        if (!hasOngoingCommunication()) {
            if (!getStateName().equals("SILENCE")) {
                _state.goToSilence();
            } else {
                throw new AlreadyIsSilentException();
            }
        }

    }

    public void goToBusy() {
        _state.goToBusy();
    }

    public void goToOff() throws AlreadyIsOffException {
        if (!isCurrentlyBusy()) {
            if (!getStateName().equals("OFF")) {
                _state.goToOff();
            } else {
                throw new AlreadyIsOffException();
            }
        }
    }

    public boolean isCurrentlyBusy(){
        return getStateName().equals("BUSY");
    }

    public void savePreviousState() {
        _previousState = _state;
    }

    public void retrievePreviousState() {

        try {
            if (_previousState.getStateName().equals("SILENCE")) {
                goToSilence();
            } else {
                goToIdle();
            }
        } catch (AlreadyIsSilentException | AlreadyIsOnException e) {
            e.printStackTrace();
        }
        _previousState = null;
    }


    public boolean startedOngoingComm() {
        if (_ongoingCommunication == null) {
            return false;
        } else {
            return _ongoingCommunication.fromTerminalId().equals(_id);
        }
    }

    public boolean hasOngoingCommunication() {
        if (_ongoingCommunication == null) {
            return false;
        } else {
            return _ongoingCommunication.getStatus();
        }
    }

    public void setOngoingCommunication(InteractiveCommunication communication) {
        _ongoingCommunication = communication;
    }

    public abstract void accept(TerminalVisitor visitor);

    public void addCommunication(Communication communication) {
        _communications.put(communication.getId(), communication);
    }

    public void addNotificationToSend(Client client, String commType) {

        ArrayList<String> tempList;

        if (_sendNotificationsTo.containsKey(client)) {
            tempList = _sendNotificationsTo.get(client);
            tempList.add(commType);
        } else {
            tempList = new ArrayList<>();
            tempList.add(commType);
            _sendNotificationsTo.put(client, tempList);
        }
    }

    public void sendNotifications(Notification notification) {

        Set<Client> localSet = new LinkedHashSet<>(_sendNotificationsTo.keySet());

        if (notification.getType().equals("O2S")) {
            for (Client client: localSet) {
                if (_sendNotificationsTo.get(client).contains("TEXT")) {
                    client.addNotifications(notification);
                    _sendNotificationsTo.get(client).remove("TEXT");
                }
            }
        } else {
            for (Client client : localSet) {
                client.addNotifications(notification);
                _sendNotificationsTo.remove(client);
            }
        }

    }

    public void setState(State newState) {
        _state = newState;
    }

    public String getId() {
        return _id;
    }

    public Client getClient() {
        return _owner;
    }

    public String getStateName() {
        return _state.getStateName();
    }

    public Set<String> getFriends(){
        return _friends.keySet();
    }

    public double getPayments() {
        return _paid;
    }

    public double getDebt() {
        return _debt;
    }

    private void addDebt(double price) {
        _debt += price;
    }

    private void addPayment(double price) {
        _paid += price;
    }

    public String getClientId() {
        return _owner.getId();
    }

    public Client getOwner() {
        return _owner;
    }

    public Collection<Communication> getCommunications() {
        return Collections.unmodifiableCollection(_communications.values());
    }

    public Collection<Communication> getSentCommunications() {

        List<Communication> sentCommunications = new ArrayList<>();

       _communications.values().stream().filter(communication -> communication.fromClientId().equals(_owner.getId()))
                .forEach(communication -> sentCommunications.add(communication));

        return Collections.unmodifiableCollection(sentCommunications);

    }

    public Collection<Communication> getReceivedCommunications() {

        List<Communication> receivedCommunications = new ArrayList<>();

        _communications.values().stream().filter(communication -> communication.toClientId().equals(_owner.getId()))
                .forEach(communication -> receivedCommunications.add(communication));

        return Collections.unmodifiableCollection(receivedCommunications);

    }

    public Communication getOngoingCommunication() throws NoOngoingCommunicationException {

        if (_ongoingCommunication != null) {
            return _ongoingCommunication;
        } else {
            throw new NoOngoingCommunicationException();
        }

    }

    public void calculateCommunicationPrice(Communication comm) {

        try {
            if (_friends.containsKey(comm.toTerminalId())) {
                comm.setPrice(_owner.calculateCommunicationPrice(comm.getType(), comm.getUnits(), true));
            } else {
                comm.setPrice(_owner.calculateCommunicationPrice(comm.getType(), comm.getUnits(), false));
            }
        } catch (UnrecognizedEntryException e) {
            e.printStackTrace();
        }

    }

    public double getLastCommunicationPrice() {
        return _lastCommunication.getPrice();
    }

    public abstract String getType();

}
