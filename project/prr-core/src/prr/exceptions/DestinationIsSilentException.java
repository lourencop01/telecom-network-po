package prr.exceptions;

public class DestinationIsSilentException extends Exception {

    private static final long serialVersionUID = 202230101409L;

    private String _destination;
    public DestinationIsSilentException(String key){
        _destination = key;
    }
}
