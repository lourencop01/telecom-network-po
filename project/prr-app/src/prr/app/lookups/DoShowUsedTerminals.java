package prr.app.lookups;

import prr.Network;
import prr.app.visit.PrintTerminal;
import prr.visitors.TerminalVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show unused terminals (without communications).
 */
class DoShowUsedTerminals extends Command<Network> {

	private final TerminalVisitor printTerminal = new PrintTerminal();

	DoShowUsedTerminals(Network receiver) {
		super(Label.SHOW_USED_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_receiver.getUsedTerminals().stream().forEach(terminal -> { terminal.accept(printTerminal);
			_display.popup(printTerminal.getLine()); });

	}
}
