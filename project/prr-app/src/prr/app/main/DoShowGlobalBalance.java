package prr.app.main;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import static java.lang.Math.round;
//FIXME add more imports if needed

/**
 * Show global balance.
 */
class DoShowGlobalBalance extends Command<Network> {

	DoShowGlobalBalance(Network receiver) {
		super(Label.SHOW_GLOBAL_BALANCE, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_display.popup(Message.globalPaymentsAndDebts(round(_receiver.getGlobalPayments()), round(_receiver.getGlobalDebt())));

	}

}
