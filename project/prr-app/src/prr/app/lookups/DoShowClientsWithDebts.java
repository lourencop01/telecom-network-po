package prr.app.lookups;

import prr.Network;
import prr.app.visit.PrintClient;
import prr.visitors.ClientVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with negative balance.
 */
class DoShowClientsWithDebts extends Command<Network> {

	private final ClientVisitor printClients = new PrintClient(false);
	DoShowClientsWithDebts(Network receiver) {
		super(Label.SHOW_CLIENTS_WITH_DEBTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_receiver.getClientsWithDebts().stream().forEach(client -> { client.accept(printClients);
			_display.popup(printClients.getLine()); });

	}
}
