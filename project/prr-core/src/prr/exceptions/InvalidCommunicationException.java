package prr.exceptions;

public class InvalidCommunicationException extends Exception{

    private int _id;

    public InvalidCommunicationException(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }
}
