package prr.app.terminal;

import prr.Network;
import prr.exceptions.InvalidCommunicationException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Perform payment.
 */
class DoPerformPayment extends TerminalCommand {

	DoPerformPayment(Network context, Terminal terminal) {
		super(Label.PERFORM_PAYMENT, context, terminal);
		addIntegerField("id", Prompt.commKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.performPayment(integerField("id"));
		} catch (InvalidCommunicationException e) {
			_display.popup(Message.invalidCommunication());
		}
	}
}
