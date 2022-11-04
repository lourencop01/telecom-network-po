package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.alreadyexists.ClientDoesNotExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import static java.lang.Math.round;

/**
 * Show the payments and debts of a client.
 */
class DoShowClientPaymentsAndDebts extends Command<Network> {

	DoShowClientPaymentsAndDebts(Network receiver) {
		super(Label.SHOW_CLIENT_BALANCE, receiver);

		addStringField("id", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {

		try {
			long payments = round(_receiver.getClientPayments(stringField("id")));
			long debt = round(_receiver.getClientDebt(stringField("id")));
			_display.popup(Message.clientPaymentsAndDebts(stringField("id"),
					payments, debt));
		} catch (ClientDoesNotExistException e) {
			throw new UnknownClientKeyException(stringField("id"));
		}

	}
}
