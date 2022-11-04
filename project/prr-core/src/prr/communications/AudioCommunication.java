package prr.communications;

import prr.terminals.Terminal;
import prr.visitors.CommunicationVisitor;

public class AudioCommunication extends InteractiveCommunication{

    public AudioCommunication(Terminal from, Terminal to, int id){
        super(from, to, id);
    }

    @Override
    public String getType(){
        return "VOICE";
    }

    @Override
    public void accept(CommunicationVisitor communicationVisitor){
        communicationVisitor.visitAudioCommunication(this);
    }

}
