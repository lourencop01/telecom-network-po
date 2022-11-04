package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

import static java.lang.Math.round;

/**
 * Command for ending communication.
 */
class DoEndInteractiveCommunication extends TerminalCommand {

	DoEndInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.END_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canEndCurrentCommunication());
		addRealField("minutes", Prompt.duration());
	}

	@Override
	protected final void execute() throws CommandException {

			_receiver.endCurrentCommunication(realField("minutes"), _network);
			_display.popup(Message.communicationCost(round(_receiver.getLastCommunicationPrice())));

	}
}
