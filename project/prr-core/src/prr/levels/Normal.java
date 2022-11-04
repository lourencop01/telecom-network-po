package prr.levels;

import prr.Client;
import prr.tariffs.BaseTariff;

public class Normal extends Level {

    public Normal(Client client) {

        super(client, new BaseTariff());

    }

    public String getLevelName() {
        return "NORMAL";
    }

    @Override
    public void shouldUpgrade() {

        if (_client.getBalance() > 500) {
            _client.setLevel(new Gold(_client));
        }

    }

    @Override
    public void shouldDegrade() {
    }

}
