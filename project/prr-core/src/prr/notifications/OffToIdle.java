package prr.notifications;

public class OffToIdle extends Notification {

    public OffToIdle(String originTerminal){
        super(originTerminal);
    }

    @Override
    public String getType(){
        return "O2I";
    }

}
