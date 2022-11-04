package prr.app.clients;

import prr.Network;

import prr.app.visit.PrintClient;
import prr.visitors.ClientVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show all clients.
 */
class DoShowAllClients extends Command<Network> {

	private final ClientVisitor printClients = new PrintClient(false);

	DoShowAllClients(Network receiver) {
		super(Label.SHOW_ALL_CLIENTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_receiver.getClients().stream().forEach(client -> {client.accept(printClients);
			_display.popup(printClients.getLine());});

	}
}

