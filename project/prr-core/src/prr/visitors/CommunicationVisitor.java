package prr.visitors;

import prr.communications.AudioCommunication;
import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;

public interface CommunicationVisitor {

    void visitTextCommunication(TextCommunication textCommunication);

    void visitAudioCommunication(AudioCommunication audioCommunication);

    void visitVideoCommunication(VideoCommunication videoCommunication);

    String getLine();

}
