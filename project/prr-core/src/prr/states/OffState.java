package prr.states;

import prr.notifications.OffToIdle;
import prr.notifications.OffToSilent;
import prr.terminals.Terminal;

public class OffState extends State {

    private static final long serialVersionUID = 202216101525L; /*is it necessary?*/
    public OffState(Terminal terminal) {

        super(terminal);

    }

    @Override
    public void goToIdle(){
        _terminal.setState(new IdleState(_terminal));
        _terminal.sendNotifications(new OffToIdle(_terminal.getId()));
    }

    @Override
    public void goToSilence(){
        _terminal.setState(new SilenceState(_terminal));
        _terminal.sendNotifications(new OffToSilent(_terminal.getId()));
    }

    @Override
    public void goToOff() {
    }

    @Override
    public void goToBusy(){

    }

    @Override
    public String getStateName(){
        return "OFF";
    }

}
