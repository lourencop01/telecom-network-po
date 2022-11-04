package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.app.visit.PrintClient;
import prr.exceptions.alreadyexists.ClientDoesNotExistException;
import prr.visitors.ClientVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

	private final ClientVisitor printClient = new PrintClient(true);

	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
		addStringField("id", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {

		try {
			_receiver.getClient(stringField("id")).accept(printClient);
			_display.popup(printClient.getLine());
		} catch (ClientDoesNotExistException e){
			throw new UnknownClientKeyException(stringField("id"));
		}
	}
}
