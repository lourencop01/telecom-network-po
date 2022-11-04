package prr.exceptions.alreadyexists;

public class ClientAlreadyExistsException extends AlreadyExistsException {

    private static final long serialVersionUID = 202216101212L;

    public ClientAlreadyExistsException(String key){

        super(key, "Client", true);

    }

}
