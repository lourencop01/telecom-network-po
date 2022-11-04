package prr.notifications;

public class SilentToIdle extends Notification {

    public SilentToIdle(String originTerminal){
        super(originTerminal);
    }

    @Override
    public String getType(){
        return "S2I";
    }

}
