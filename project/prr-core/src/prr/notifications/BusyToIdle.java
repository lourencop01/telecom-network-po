package prr.notifications;

public class BusyToIdle extends Notification {

    public BusyToIdle(String originTerminal){
        super(originTerminal);
    }

    @Override
    public String getType(){
        return "B2I";
    }
}
