package prr.tariffs;

import prr.exceptions.UnrecognizedEntryException;

public class BaseTariff implements Tariff{

    private static final long serialVersionUID = 202231101008L;

    @Override
    public double calculatePrice(String type, double size, boolean areFriends, String level) throws UnrecognizedEntryException {

        return  switch (type) {
            case "TEXT" -> textMessage(size, level);
            case "VOICE" -> voiceMessage(size, level, areFriends);
            case "VIDEO" -> videoMessage(size, level, areFriends);
            default -> throw new UnrecognizedEntryException(type);
        };

    }

    @Override
    public double textMessage(double length, String level) throws UnrecognizedEntryException {

        return switch (level) {
                case "NORMAL" -> normalTextMessage(length);
                case "GOLD" -> goldTextMessage(length);
                case "PLATINUM" -> platinumTextMessage(length);
            default -> throw new UnrecognizedEntryException(level);
        };

    }

    @Override
    public double normalTextMessage(double length) {

        if (length < 50) {
            return 10;
        } else if (length < 100) {
            return 16;
        } else if (length >= 100) {
            return 2*length;
        } else
            return -1;

    }

    @Override
    public double goldTextMessage(double length) {

        if (length < 50) {
            return 10;
        } else if (length < 100) {
            return 10;
        } else if (length >= 100) {
            return 2*length;
        } else
            return -1;

    }

    @Override
    public double platinumTextMessage(double length) {

        if (length < 50) {
            return 0;
        } else if (length >= 50) {
            return 4;
        } else
            return -1;

    }

    @Override
    public double voiceMessage(double duration, String level, boolean areFriends) throws UnrecognizedEntryException {

        return switch (level) {
            case "NORMAL" -> normalVoiceMessage(duration, areFriends);
            case "GOLD" -> goldVoiceMessage(duration, areFriends);
            case "PLATINUM" -> platinumVoiceMessage(duration, areFriends);
            default -> throw new UnrecognizedEntryException(level);
        };

    }

    @Override
    public double normalVoiceMessage(double duration, boolean areFriends) {

            if (areFriends) {
                return 20 * duration * 0.5;
            }
            return 20 * duration;
    }

    @Override
    public double goldVoiceMessage(double duration, boolean areFriends) {

            if (areFriends) {
                return 10 * duration * 0.5;
            }
            return 10 * duration;
    }

    @Override
    public double platinumVoiceMessage(double duration, boolean areFriends) {

            if (areFriends) {
                return 10 * duration * 0.5;
            }
            return 10 * duration;
    }


    @Override
    public double videoMessage(double duration, String level, boolean areFriends) throws UnrecognizedEntryException {

        return switch (level) {
            case "NORMAL" -> normalVideoMessage(duration, areFriends);
            case "GOLD" -> goldVideoMessage(duration, areFriends);
            case "PLATINUM" -> platinumVideoMessage(duration, areFriends);
            default -> throw new UnrecognizedEntryException(level);
        };

    }

    @Override
    public double normalVideoMessage(double duration, boolean areFriends) {

            if (areFriends) {
                return 30 * duration * 0.5;
            }
            return 30 * duration;
    }

    @Override
    public double goldVideoMessage(double duration, boolean areFriends) {

            if (areFriends) {
                return 20 * duration * 0.5;
            }
            return 20 * duration;
    }

    @Override
    public double platinumVideoMessage(double duration, boolean areFriends) {

            if (areFriends) {
                return 10 * duration * 0.5;
            }
            return 10 * duration;
    }

}
