package prr.exceptions;

public class DestinationIsOffException extends Exception {

    private static final long serialVersionUID = 202230101301L;

    private String _destination;
    public DestinationIsOffException(String key){
        _destination = key;
    }
}
