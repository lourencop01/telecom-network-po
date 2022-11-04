package prr.exceptions.alreadyexists;

public class AlreadyExistsException extends Exception {

    private static final long serialVersionUID = 202216101212L;

    private final String _key;

    private final String _type;

    private final boolean _exists;

    public AlreadyExistsException(String key, String type, boolean bool){

        _key = key;
        _type = type;
        _exists = bool;

    }

    public String getKey(){

        return _key;

    }

    public String getType(){

        return _type;

    }

    public boolean exists(){
        return _exists;
    }

}
