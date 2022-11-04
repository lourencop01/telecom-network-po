package prr.exceptions.alreadyexists;

public class ClientDoesNotExistException extends AlreadyExistsException {

    private static final long serialVersionUID = 202216101212L;

    public ClientDoesNotExistException(String key){
        super(key, "Client", false);
    }

}
