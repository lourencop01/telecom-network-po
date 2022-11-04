package prr.app.terminal;

import prr.Network;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.alreadyexists.TerminalDoesNotExistException;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

        DoSendTextCommunication(Network context, Terminal terminal) {
                super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
                addStringField("id", Prompt.terminalKey());
                addStringField("message", Prompt.textMessage());
        }

        @Override
        protected final void execute() throws CommandException {

                try {
                        _receiver.newTextMessage(stringField("message"), stringField("id"), _network);
                } catch (TerminalDoesNotExistException e) {
                        throw new UnknownTerminalKeyException(stringField("id"));
                } catch (DestinationIsOffException e) {
                        _display.popup(Message.destinationIsOff(stringField("id")));
                }

        }
} 
