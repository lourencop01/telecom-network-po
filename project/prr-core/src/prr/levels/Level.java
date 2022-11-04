package prr.levels;

import prr.Client;
import prr.exceptions.UnrecognizedEntryException;
import prr.tariffs.Tariff;

import java.io.Serializable;

public abstract class Level implements Serializable {

    protected Client _client;

    private Tariff _tariff;

    public Level(Client client, Tariff tariff){
        _client = client;
        _tariff = tariff;
    }

    public abstract String getLevelName();

    public abstract void shouldUpgrade();

    public abstract void shouldDegrade();

    public double calculatePrice(String type, double size, boolean areFriends) throws UnrecognizedEntryException {
        return _tariff.calculatePrice(type, size, areFriends, _client.getLevelName());
    }

}
