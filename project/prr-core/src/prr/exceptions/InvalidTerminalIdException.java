package prr.exceptions;

public class InvalidTerminalIdException extends Exception {

    private static final long serialVersionUID = 202217101811L;

    private final String _key;

    public InvalidTerminalIdException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
