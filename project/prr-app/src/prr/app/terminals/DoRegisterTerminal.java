package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.InvalidTerminalIdException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.alreadyexists.ClientDoesNotExistException;
import prr.exceptions.alreadyexists.TerminalAlreadyExistsException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);

		String[] options = {"BASIC", "FANCY"};

		addStringField("id", Prompt.terminalKey());
		addOptionField("type", Prompt.terminalType(), options);
		addStringField("clientId", Prompt.clientKey());

	}

	@Override
	protected final void execute() throws CommandException {


		String[] fields = {"", "", "", "ON"};

		fields[0] = optionField("type");
		fields[1] = stringField("id");
		fields[2] = stringField("clientId");


		try{
			_receiver.registerTerminal(fields[0], fields[1], fields[2], fields[3]);
		} catch (UnrecognizedEntryException e) {
			e.printStackTrace();
		} catch (InvalidTerminalIdException e) {
			throw new InvalidTerminalKeyException(fields[1]);
		} catch (TerminalAlreadyExistsException e) {
			throw new DuplicateTerminalKeyException(fields[1]);
		} catch (ClientDoesNotExistException e){
			throw new UnknownClientKeyException(fields[2]);
		}
	}
}
