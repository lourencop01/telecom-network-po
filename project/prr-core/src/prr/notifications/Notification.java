package prr.notifications;

public abstract class Notification {

    private String _originTerminal;

    public Notification(String originTerminal){
        _originTerminal = originTerminal;
    }

    public abstract String getType();

    public String getOriginTerminal(){
        return _originTerminal;
    }



}
