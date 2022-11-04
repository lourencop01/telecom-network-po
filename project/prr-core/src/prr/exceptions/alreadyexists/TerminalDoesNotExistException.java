package prr.exceptions.alreadyexists;

public class TerminalDoesNotExistException extends AlreadyExistsException{

    private static final long serialVersionUID = 202216101212L;

    public TerminalDoesNotExistException(String key){

        super(key, "Terminal", false);

    }

}
