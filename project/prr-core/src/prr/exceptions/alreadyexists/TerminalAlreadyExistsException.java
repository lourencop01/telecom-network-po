package prr.exceptions.alreadyexists;

public class TerminalAlreadyExistsException extends AlreadyExistsException{

    private static final long serialVersionUID = 202216101212L;

    public TerminalAlreadyExistsException(String key){

        super(key, "Terminal", true);

    }

}
