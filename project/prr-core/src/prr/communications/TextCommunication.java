package prr.communications;

import prr.terminals.Terminal;
import prr.visitors.CommunicationVisitor;

public class TextCommunication extends Communication{

    private String _message;

    public TextCommunication(Terminal from, Terminal to, int id, String message){

        super(from, to, id, false);
        _message = message;

    }

    @Override
    public String getType(){
        return "TEXT";
    }

    @Override
    public double getUnits(){
        return _message.length();
    }

    @Override
    public void accept(CommunicationVisitor communicationVisitor){
        communicationVisitor.visitTextCommunication(this);
    }

}
