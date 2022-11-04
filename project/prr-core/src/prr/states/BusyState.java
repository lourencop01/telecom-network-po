package prr.states;

import prr.notifications.BusyToIdle;
import prr.terminals.Terminal;

public class BusyState extends State {

    private static final long serialVersionUID = 202216101525L; /*is it necessary?*/
    public BusyState(Terminal terminal) {

        super(terminal);

    }

    @Override
    public void goToIdle(){
        _terminal.setState(new IdleState(_terminal));
        _terminal.sendNotifications(new BusyToIdle(_terminal.getId()));
    }

    @Override
    public void goToSilence(){
        _terminal.setState(new SilenceState(_terminal));
    }

    @Override
    public void goToOff(){
    }

    @Override
    public void goToBusy() {
    }

    @Override
    public String getStateName(){
        return "BUSY";
    }

}
