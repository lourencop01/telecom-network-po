package prr.app.terminals;

import prr.Network;
import prr.app.visit.PrintTerminal;
import prr.visitors.TerminalVisitor;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show all terminals.
 */
class DoShowAllTerminals extends Command<Network> {

	private final TerminalVisitor printTerminal = new PrintTerminal();
	DoShowAllTerminals(Network receiver) {
		super(Label.SHOW_ALL_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {

		_receiver.getTerminals().stream().forEach(terminal -> {terminal.accept(printTerminal);
												_display.popup(printTerminal.getLine());});

	}
}
