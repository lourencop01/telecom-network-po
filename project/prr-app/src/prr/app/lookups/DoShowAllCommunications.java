package prr.app.lookups;

import prr.Network;
import prr.app.visit.PrintCommunication;
import prr.visitors.CommunicationVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for showing all communications.
 */
class DoShowAllCommunications extends Command<Network> {

	private final CommunicationVisitor printCommunication = new PrintCommunication();
	DoShowAllCommunications(Network receiver) {
		super(Label.SHOW_ALL_COMMUNICATIONS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_receiver.getCommunications().stream().forEach(communication -> {communication.accept(printCommunication);
			_display.popup(printCommunication.getLine());});

	}
}
