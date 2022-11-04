package prr.app.lookups;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.app.visit.PrintCommunication;
import prr.exceptions.alreadyexists.ClientDoesNotExistException;
import prr.visitors.CommunicationVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show communications to a client.
 */
class DoShowCommunicationsToClient extends Command<Network> {

	private final CommunicationVisitor printCommunication = new PrintCommunication();
	DoShowCommunicationsToClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_TO_CLIENT, receiver);
		addStringField("id", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {

		try {
			_receiver.getCommunicationsToClient(stringField("id")).stream()
					.forEach(communication -> { communication.accept(printCommunication);
						_display.popup(printCommunication.getLine()); });
		} catch (ClientDoesNotExistException e){
			throw new UnknownClientKeyException(stringField("id"));
		}

	}
}
