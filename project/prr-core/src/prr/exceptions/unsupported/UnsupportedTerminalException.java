package prr.exceptions.unsupported;

public class UnsupportedTerminalException extends Exception {

    private static final long serialVersionUID = 202230101537L;

    private String _terminalId, _type;

    public UnsupportedTerminalException(String id, String type){
        _terminalId = id;
        _type = type;
    }

    public String getTerminalId() {
        return _terminalId;
    }

    public String getTerminalType() {
        return _type;
    }

}
