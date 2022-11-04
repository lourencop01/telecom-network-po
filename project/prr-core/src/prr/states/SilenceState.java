package prr.states;

import prr.notifications.SilentToIdle;
import prr.terminals.Terminal;

public class SilenceState extends State {

    private static final long serialVersionUID = 202216101525L; /*is it necessary?*/

    public SilenceState(Terminal terminal) {

        super(terminal);

    }

    @Override
    public void goToIdle() {
        _terminal.setState(new IdleState(_terminal));
        _terminal.sendNotifications(new SilentToIdle(_terminal.getId()));
    }

    @Override
    public void goToSilence() {
    }

    @Override
    public void goToOff() {
        _terminal.setState(new OffState(_terminal));
    }

    @Override
    public void goToBusy() {
        _terminal.setState(new BusyState(_terminal));
    }

    @Override
    public String getStateName(){
        return "SILENCE";
    }

}
