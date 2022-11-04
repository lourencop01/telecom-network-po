package prr.notifications;

public class OffToSilent extends Notification {

    public OffToSilent(String originTerminal){
        super(originTerminal);
    }

    @Override
    public String getType(){
        return "O2S";
    }

}
