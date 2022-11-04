package prr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import prr.communications.AudioCommunication;
import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.exceptions.ImportFileException;
import prr.exceptions.InvalidTerminalIdException;
import prr.exceptions.UnrecognizedEntryException;

import prr.exceptions.alreadyexists.AlreadyExistsException;
import prr.exceptions.alreadyexists.ClientAlreadyExistsException;
import prr.exceptions.alreadyexists.ClientDoesNotExistException;
import prr.exceptions.alreadyexists.TerminalAlreadyExistsException;
import prr.exceptions.alreadyexists.TerminalDoesNotExistException;
import prr.states.BusyState;
import prr.states.OffState;
import prr.states.IdleState;
import prr.states.SilenceState;

import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.terminals.Terminal;

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

    /**
     * Serial number for serialization.
     */
    private static final long serialVersionUID = 202208091753L;

    /**
     * Map of all clients
     */
    private Map<String, Client> _clients;

    /**
     * Map of all terminals
     */
    private Map<String, Terminal> _terminals;

    /**
     * Map of all communications
     */
    private Map<Integer, Communication> _communications;

    /**
     * Index counter for communications
     */
    private int _commCounter;

    /**
     * Boolean that relates to the changes of the database
     */
    private boolean _changed;

    /**
     * Network constructor - initializes variables contained in the network
     */
    public Network() {

        _clients = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _terminals = new TreeMap<>();
        _communications = new TreeMap<>();
        _changed = false;
        _commCounter = 0;

    }

    /**
     * Read text input file and create corresponding domain entities.
     *
     * @param filename - name of the text input file
     * @throws ImportFileException if some problem with the import file
     */
    void importFile(String filename) throws ImportFileException {

        try (BufferedReader buffRead = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = buffRead.readLine()) != null) {
                String[] fields = line.split("\\|");
                try {
                    registerEntry(fields);
                } catch (AlreadyExistsException | InvalidTerminalIdException | UnrecognizedEntryException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new ImportFileException(filename);
        }
    }

    /**
     * Reads the fields and decides what method to access after
     *
     * @param fields
     * @throws UnrecognizedEntryException if the entry is unknown
     * @throws AlreadyExistsException if the "entry" already exists in the database
     * @throws InvalidTerminalIdException if the id is not composed of 6 numbers
     */
    public void registerEntry(String... fields) throws UnrecognizedEntryException, AlreadyExistsException,
            InvalidTerminalIdException {

        switch (fields[0]) {
            case "CLIENT" -> registerClient(fields);
            case "BASIC", "FANCY" -> registerTerminal(fields);
            case "FRIENDS" -> makeFriends(fields);
            default -> throw new UnrecognizedEntryException(fields[0]);
        }

        setChanged(true);

    }

    /**
     * Creates a new Client with the information (fields) given
     *
     * @param fields - fields[0]: "CLIENT", fields[1]: Id, fields[2]: Name, fields[3]: Nif
     * @throws ClientAlreadyExistsException if the client is already in the database
     */
    public void registerClient(String... fields) throws ClientAlreadyExistsException {

        int nif = Integer.parseInt(fields[3]);

        Client client = new Client(fields[1], fields[2], nif);
        addClient(client);

        setChanged(true);

    }

    /**
     * Adds a client to the map of clients (_clients)
     *
     * @param client the client
     * @throws ClientAlreadyExistsException if the client is already in the database
     */
    public void addClient(Client client) throws ClientAlreadyExistsException {

        if (_clients.containsKey(client.getId())) {
            throw new ClientAlreadyExistsException(client.getId());
        } else {
            _clients.put(client.getId(), client);
        }
    }

    /**
     * Creates a new Terminal with the information (fields) given
     *
     * @param fields - fields[0]: "FANCY" or "BASIC", fields[1]: Id, fields[2]: Terminal owner, fields[3]: State
     * @throws UnrecognizedEntryException if the entry is unknown
     * @throws TerminalAlreadyExistsException if the terminal is already in the database
     * @throws InvalidTerminalIdException if the id is not composed of 6 numbers
     * @throws ClientDoesNotExistException if the terminal owner does not exist
     */
    public void registerTerminal(String... fields) throws UnrecognizedEntryException, TerminalAlreadyExistsException,
            InvalidTerminalIdException, ClientDoesNotExistException {

        Terminal terminal = null;

        if (fields[1].length() != 6) {
            throw new InvalidTerminalIdException(fields[1]);
        }

        try {
            Integer.parseInt(fields[1]);
        } catch (NumberFormatException e){
            throw new InvalidTerminalIdException(fields[1]);
        }

        terminal = switch (fields[0]) {
            case "BASIC" -> new BasicTerminal(fields[1], _clients.get(fields[2]));
            case "FANCY" -> new FancyTerminal(fields[1], _clients.get(fields[2]));
            default -> throw new UnrecognizedEntryException(fields[0]);
        };

            switch (fields[3]) {
                case "ON" -> terminal.setState(new IdleState(terminal));
                case "OFF" -> terminal.setState(new OffState(terminal));
                case "SILENCE" -> terminal.setState(new SilenceState(terminal));
                case "BUSY" -> terminal.setState(new BusyState(terminal));
                default -> throw new UnrecognizedEntryException(fields[3]);
            }

        addTerminal(terminal, fields[2]);
        setChanged(true);

    }

    /**
     * Adds a terminal to the map of clients (_terminals) and the terminal to the map of terminals of the owner
     *
     * @param terminal the terminal
     * @param clientId the terminal owner
     * @throws TerminalAlreadyExistsException if the terminal is already in the database
     * @throws ClientDoesNotExistException if the terminal owner is not in the database
     */
    public void addTerminal(Terminal terminal, String clientId) throws TerminalAlreadyExistsException,
            ClientDoesNotExistException {

        if (_terminals.containsKey(terminal.getId())) {
            throw new TerminalAlreadyExistsException(terminal.getId());
        } else if (!_clients.containsKey(clientId)) {
            throw new ClientDoesNotExistException(clientId);
        } else {
            _terminals.put(terminal.getId(), terminal);
            _clients.get(clientId).putTerminal(terminal);
        }

    }

    /**
     * Adds a friend to a certain terminal
     *
     * @param fields - fields[0]: "FRIENDS", fields[1]: the terminal id, fields[2]: friends
     */
    public void makeFriends(String... fields) throws TerminalDoesNotExistException {

        String[] friends = fields[2].split(",");

        for (String friend : friends) {
            _terminals.get(fields[1]).addFriend(friend, this);
        }

    }

    /**
     * Creates a new Communication with the information (fields) given and inserts it in the Map of communications of
     * the network, and the from and to terminals
     *
     * @param fields - fields[0]: "TEXT", "VOICE", or "VIDEO", fields[1]: Terminal from, fields[2]: Terminal to,
     *               fields[3]: message
     * @throws UnrecognizedEntryException if the entry is unknown
     * @throws TerminalDoesNotExistException if any of the terminals is not in the database
     */
    public void registerCommunication(String... fields) throws TerminalDoesNotExistException, UnrecognizedEntryException {

        Communication newComm = switch (fields[0]) {
                                case "TEXT" -> new TextCommunication(getTerminal(fields[1]),
                                        getTerminal(fields[2]), idPlusOne(), fields[3]);
                                case "VOICE" -> new AudioCommunication(getTerminal(fields[1]), getTerminal(fields[2]),
                                        idPlusOne());
                                case "VIDEO" -> new VideoCommunication(getTerminal(fields[1]), getTerminal(fields[2]),
                                        idPlusOne());
                                default -> throw new UnrecognizedEntryException(fields[0]);
        };

        _communications.put(newComm.getId(), newComm);
        getTerminal(fields[1]).addCommunication(newComm);
        getTerminal(fields[2]).addCommunication(newComm);
        setChanged(true);
    }

    /**
     * @return true if the database has changed, false otherwise
     */
    public boolean hasChanged() {
        return _changed;
    }

    /**
     * sets the _changed variable to true if the database has changed, false otherwise
     *
     * @param changed
     */
    public void setChanged(boolean changed){
        _changed = changed;
    }

    /**
     * Increments the communication counter by one
     */
    public int idPlusOne() {
        return _commCounter+=1;
    }

    /**
     * @param id client's id
     * @throws ClientDoesNotExistException if the client is not in the database
     * @return a selected client from the database
     */
    public Client getClient(String id) throws ClientDoesNotExistException {

        if(!_clients.containsKey(id)) {
            throw new ClientDoesNotExistException(id);
        }

        return _clients.get(id);
    }

    /**
     * @return an unmodifiable collection of the databases' clients
     */
    public Collection<Client> getClients() {
        return Collections.unmodifiableCollection(_clients.values());
    }

    /**
     * @param id - client's id
     * @return true if the client can receive notifications, false if not
     */
    public boolean getClientNotificationState(String id) throws ClientDoesNotExistException {
        return getClient(id).getNotificationState();
    }

    /**
     * Turns the notification state of a certain client on or off, depending on the parameter value
     *
     * @param id - client's id
     * @param value  - true or false
     */
    public void setClientNotificationState(String id, boolean value) throws ClientDoesNotExistException {
        getClient(id).setNotificationState(value);
    }

    /**
     * @param id - client's id
     *
     * @return the total amount paid by the client
     */
    public double getClientPayments(String id) throws ClientDoesNotExistException {
        return getClient(id).getPayments();
    }

    /**
     * @param id - client's id
     *
     * @return the total amount indebted by the client
     */
    public double getClientDebt(String id) throws ClientDoesNotExistException {
        return getClient(id).getDebt();
    }

    /**
     * @return an unmodifiable collection of clients that have debts
     */
    public Collection<Client> getClientsWithDebts(){

        List<Client> clientsWithDebt = new ArrayList<>();

        getClients().stream().filter(client -> client.getDebt() > 0).forEach(client -> clientsWithDebt.add(client));

        return Collections.unmodifiableCollection(clientsWithDebt);
    }

    /**
     * @return an unmodifiable collection of clients that do not have debts
     */
    public Collection<Client> getClientsWithoutDebts(){

        List<Client> clientsWithoutDebt = new ArrayList<>();

        getClients().stream().filter(client -> client.getDebt() == 0).forEach(client -> clientsWithoutDebt.add(client));

        return Collections.unmodifiableCollection(clientsWithoutDebt);
    }

    /**
     * @param id terminal's id
     * @throws TerminalDoesNotExistException if the terminal is not in the database
     * @return a selected terminal from the database
     */
    public Terminal getTerminal(String id) throws TerminalDoesNotExistException {

        if(!_terminals.containsKey(id)) {
            throw new TerminalDoesNotExistException(id);
        }
        return _terminals.get(id);

    }

    /**
     * @return an unmodifiable collection of the databases' terminals
     */
    public Collection<Terminal> getTerminals() {
        return Collections.unmodifiableCollection(_terminals.values());
    }

    /**
     * @return an unmodifiable collection of terminals which have not yet performed a communication
     */
    public Collection<Terminal> getUnusedTerminals(){

        List<Terminal> unused = new ArrayList<>();

        getTerminals().stream().filter(terminal -> terminal.getCommunications().size() == 0)
                .forEach(terminal -> unused.add(terminal));

        return Collections.unmodifiableCollection(unused);
    }

    /**
     * @return an unmodifiable collection of terminals which have performed a communication
     */
    public Collection<Terminal> getUsedTerminals(){

        List<Terminal> used = new ArrayList<>();

        getTerminals().stream().filter(terminal -> terminal.getCommunications().size() != 0)
                .forEach(terminal -> used.add(terminal));

        return Collections.unmodifiableCollection(used);
    }

    /**
     * @return an unmodifiable collection of terminals that have a positive balance (paid more than they owe)
     */
    public Collection<Terminal> getTerminalsWithPositiveBalance(){

        List<Terminal> terminalsWithPositiveBalance = new ArrayList<>();

        getTerminals().stream().filter(terminal -> terminal.getPayments() - terminal.getDebt() > 0)
                .forEach(terminal -> terminalsWithPositiveBalance.add(terminal));

        return Collections.unmodifiableCollection(terminalsWithPositiveBalance);
    }

    /**
     * @return an unmodifiable collection of the databases' communications
     */
    public Collection<Communication> getCommunications(){
        return Collections.unmodifiableCollection(_communications.values());
    }

    /**
     * @return the last registered communication
     */
    public Communication getLastCommunication(){
        return _communications.get(_commCounter);
    }

    /**
     * @param id - the client's id
     * @return an unmodifiable collection of the communications started by the client
     */
    public Collection<Communication> getCommunicationsFromClient(String id) throws ClientDoesNotExistException{
        return Collections.unmodifiableCollection(getClient(id).sentCommunications());
    }

    /**
     * @param id - the client's id
     * @return an unmodifiable collection of the communications received by the client
     */
    public Collection<Communication> getCommunicationsToClient(String id) throws ClientDoesNotExistException{
        return Collections.unmodifiableCollection(getClient(id).receivedCommunications());
    }

    /**
     * @return the amount owed to the network
     */
    public double getGlobalDebt(){

        double debt = 0;

        for (Client client: getClients()){
            debt += client.getDebt();
        }

        return debt;
    }

    /**
     * @return the amount paid to the network
     */
    public double getGlobalPayments(){

        double payments = 0;

        for (Client client: getClients()){
            payments += client.getPayments();
        }

        return payments;
    }

}

