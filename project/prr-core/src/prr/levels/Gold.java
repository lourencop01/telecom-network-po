package prr.levels;

import prr.Client;
import prr.tariffs.BaseTariff;

public class Gold extends Level {

    public Gold(Client client) {
        super(client, new BaseTariff());
    }

    public String getLevelName() {
        return "GOLD";
    }

    @Override
    public void shouldUpgrade() {

        if (_client.lastXCommunicationsAreType(5, "VIDEO") && _client.getBalance() >= 0) {
            _client.setLevel(new Platinum(_client));
        }

    }

    @Override
    public void shouldDegrade() {
        if (_client.getBalance() < 0) {
            _client.setLevel(new Normal(_client));
        }
    }

}
