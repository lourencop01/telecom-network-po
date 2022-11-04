package prr.exceptions.alreadyisatstate;

public class AlreadyIsException extends Exception{

    private String _stateName;

    public AlreadyIsException(String stateName) {
        _stateName = stateName;
    }

    public String getStateName() {
        return _stateName;
    }

}
