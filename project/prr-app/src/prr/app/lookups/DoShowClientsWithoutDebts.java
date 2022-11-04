package prr.app.lookups;

import prr.Network;
import prr.app.visit.PrintClient;
import prr.visitors.ClientVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with positive balance.
 */
class DoShowClientsWithoutDebts extends Command<Network> {

	private final ClientVisitor printClients = new PrintClient(false);
	DoShowClientsWithoutDebts(Network receiver) {
		super(Label.SHOW_CLIENTS_WITHOUT_DEBTS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_receiver.getClientsWithoutDebts().stream().forEach(client -> { client.accept(printClients);
			_display.popup(printClients.getLine()); });

	}
}
