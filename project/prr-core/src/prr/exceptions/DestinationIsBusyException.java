package prr.exceptions;

public class DestinationIsBusyException extends Exception {

    private static final long serialVersionUID = 202230101408L;

    private String _destination;
    public DestinationIsBusyException(String key){
        _destination = key;
    }
}
