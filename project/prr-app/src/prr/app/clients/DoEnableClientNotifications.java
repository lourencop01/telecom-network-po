package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.alreadyexists.ClientDoesNotExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


/**
 * Enable client notifications.
 */
class DoEnableClientNotifications extends Command<Network> {

	DoEnableClientNotifications(Network receiver) {
		super(Label.ENABLE_CLIENT_NOTIFICATIONS, receiver);

		addStringField("id", Prompt.key());

	}

	@Override
	protected final void execute() throws CommandException {

		try {
			if (_receiver.getClientNotificationState(stringField("id"))){
				_display.popup(Message.clientNotificationsAlreadyEnabled());
			} else {
				_receiver.setClientNotificationState(stringField("id"), true);
			}
		} catch (ClientDoesNotExistException e){
			throw new UnknownClientKeyException(stringField("id"));
		}




	}
}
