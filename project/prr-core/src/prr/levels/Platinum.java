package prr.levels;

import prr.Client;
import prr.tariffs.BaseTariff;

public class Platinum extends Level {

    public Platinum(Client client) {

        super(client, new BaseTariff());

    }

    public String getLevelName() {
        return "PLATINUM";
    }

    @Override
    public void shouldUpgrade() {
    }

    @Override
    public void shouldDegrade() {

        if ((_client.lastXCommunicationsAreType(2, "TEXT") && _client.getBalance() >= 0)) {
            _client.setLevel(new Gold(_client));
        } else if (_client.getBalance() < 0) {
            _client.setLevel(new Normal(_client));
        }

    }

}
