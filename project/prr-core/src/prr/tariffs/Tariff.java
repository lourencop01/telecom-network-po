package prr.tariffs;

import prr.exceptions.UnrecognizedEntryException;

import java.io.Serializable;

public interface Tariff extends Serializable {

    public double calculatePrice(String type, double size, boolean areFriends, String level) throws UnrecognizedEntryException;

    public double textMessage(double length, String level) throws UnrecognizedEntryException;
    public double normalTextMessage(double length);
    public double goldTextMessage(double length);
    public double platinumTextMessage(double length);

    public double voiceMessage(double duration, String level, boolean areFriends) throws UnrecognizedEntryException;
    public double normalVoiceMessage(double duration, boolean areFriends);
    public double goldVoiceMessage(double duration, boolean areFriends);
    public double platinumVoiceMessage(double duration, boolean areFriends);

    double videoMessage(double duration, String level, boolean areFriends) throws UnrecognizedEntryException;
    public double normalVideoMessage(double duration, boolean areFriends);
    public double goldVideoMessage(double duration, boolean areFriends);
    public double platinumVideoMessage(double duration, boolean areFriends);

}
