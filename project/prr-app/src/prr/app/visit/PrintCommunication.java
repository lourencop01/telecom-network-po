package prr.app.visit;

import prr.communications.AudioCommunication;
import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.visitors.CommunicationVisitor;

import static java.lang.Math.round;

public class PrintCommunication implements CommunicationVisitor {

    private String _line;

    private String _dash = "|";

    @Override
    public void visitTextCommunication(TextCommunication textCommunication){
        _line = getCharacteristics(textCommunication);
    }

    @Override
    public void visitAudioCommunication(AudioCommunication audioCommunication){
        _line = getCharacteristics(audioCommunication);
    }

    @Override
    public void visitVideoCommunication(VideoCommunication videoCommunication){
        _line = getCharacteristics(videoCommunication);
    }

    private String getCharacteristics(Communication communication){
        return communication.getType() + _dash + communication.getId() + _dash + communication.fromTerminalId() + _dash +
                communication.toTerminalId() + _dash + (communication.getStatus() ? "0|0|ONGOING" :
                (round(communication.getUnits()) + _dash + round(communication.getPrice()) + _dash + "FINISHED"));
    }

    @Override
    public String getLine(){
        return _line;
    }

}
