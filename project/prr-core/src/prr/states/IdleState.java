package prr.states;

import prr.terminals.Terminal;

public class IdleState extends State {

    private static final long serialVersionUID = 202216101525L;
    public IdleState(Terminal terminal) {

        super(terminal);

    }

    @Override
    public void goToIdle() {
    }

    @Override
    public void goToSilence(){
        _terminal.setState(new SilenceState(_terminal));
    }

    @Override
    public void goToOff(){
        _terminal.setState(new OffState(_terminal));
    }

    @Override
    public void goToBusy(){
        _terminal.setState(new BusyState(_terminal));
    }

    @Override
    public String getStateName(){
        return "IDLE";
    }

}
