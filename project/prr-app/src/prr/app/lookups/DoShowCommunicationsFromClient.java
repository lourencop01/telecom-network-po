package prr.app.lookups;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.app.visit.PrintCommunication;
import prr.exceptions.alreadyexists.ClientDoesNotExistException;
import prr.visitors.CommunicationVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

	private final CommunicationVisitor printCommunication = new PrintCommunication();
	DoShowCommunicationsFromClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
		addStringField("id", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {

		try {
		_receiver.getCommunicationsFromClient(stringField("id")).stream()
				.forEach(communication -> { communication.accept(printCommunication);
					_display.popup(printCommunication.getLine()); });
		} catch (ClientDoesNotExistException e){
			throw new UnknownClientKeyException(stringField("id"));
		}

	}
}
