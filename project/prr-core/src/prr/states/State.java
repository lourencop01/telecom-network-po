package prr.states;

import prr.terminals.Terminal;

import java.io.Serializable;

public abstract class State implements Serializable {

    private static final long serialVersionUID = 202216101525L;

    protected Terminal _terminal;

    public State(Terminal terminal) {

        _terminal = terminal;

    }

    public abstract void goToIdle();
    public abstract void goToSilence();
    public abstract void goToOff();
    public abstract void goToBusy();
    public abstract String getStateName();


}
