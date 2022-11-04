package prr.app.terminal;

import prr.Network;
import prr.app.visit.PrintCommunication;
import prr.exceptions.NoOngoingCommunicationException;
import prr.terminals.Terminal;
import prr.visitors.CommunicationVisitor;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for showing the ongoing communication.
 */
class DoShowOngoingCommunication extends TerminalCommand {

	private CommunicationVisitor communicationVisitor = new PrintCommunication();
	DoShowOngoingCommunication(Network context, Terminal terminal) {
		super(Label.SHOW_ONGOING_COMMUNICATION, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {

		try {
			_receiver.getOngoingCommunication().accept(communicationVisitor);
			_display.popup(communicationVisitor.getLine());
		} catch (NoOngoingCommunicationException e){
			_display.popup(Message.noOngoingCommunication());
		}

	}
}
