package prr.app.lookups;

import prr.Network;
import prr.app.visit.PrintTerminal;
import prr.visitors.TerminalVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show terminals with positive balance.
 */
class DoShowTerminalsWithPositiveBalance extends Command<Network> {

	private TerminalVisitor printTerminal = new PrintTerminal();
	DoShowTerminalsWithPositiveBalance(Network receiver) {
		super(Label.SHOW_TERMINALS_WITH_POSITIVE_BALANCE, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_receiver.getTerminalsWithPositiveBalance().stream().forEach(terminal -> { terminal.accept(printTerminal);
			_display.popup(printTerminal.getLine()); });

	}
}
