package prr.communications;

import prr.terminals.Terminal;
import prr.visitors.CommunicationVisitor;

public class VideoCommunication extends InteractiveCommunication{

    public VideoCommunication(Terminal from, Terminal to, int id){
        super(from, to, id);
    }

    @Override
    public String getType(){
        return "VIDEO";
    }

    @Override
    public void accept(CommunicationVisitor communicationVisitor){
        communicationVisitor.visitVideoCommunication(this);
    }

}
