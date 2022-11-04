package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.exceptions.alreadyexists.TerminalDoesNotExistException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
		addStringField("id", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {

		try {
			_receiver.addFriend(stringField("id"), _network);
		} catch (TerminalDoesNotExistException e){
			throw new UnknownTerminalKeyException(stringField("id"));
		}
	}
}
