package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.unsupported.UnsupportedDestinationTerminalException;
import prr.exceptions.unsupported.UnsupportedOriginTerminalException;
import prr.exceptions.alreadyexists.TerminalDoesNotExistException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());

		String[] options = {"VIDEO", "VOICE"};

		addStringField("id", Prompt.terminalKey());
		addOptionField("type", Prompt.commType(), options);

	}

	@Override
	protected final void execute() throws CommandException {

		try {
			_receiver.newInteractiveMessage(optionField("type"), stringField("id"), _network);
		} catch (TerminalDoesNotExistException e) {
			throw new UnknownTerminalKeyException(stringField("id"));
		} catch (DestinationIsOffException e) {
			_display.popup(Message.destinationIsOff(stringField("id")));
		} catch (DestinationIsBusyException e) {
			_display.popup(Message.destinationIsBusy(stringField("id")));
		} catch (DestinationIsSilentException e) {
			_display.popup(Message.destinationIsSilent(stringField("id")));
		} catch (UnsupportedOriginTerminalException e) {
			_display.popup(Message.unsupportedAtOrigin(e.getTerminalId(), e.getTerminalType()));
		} catch (UnsupportedDestinationTerminalException e) {
			_display.popup(Message.unsupportedAtOrigin(e.getTerminalId(), e.getTerminalType()));
		}
	}
}
