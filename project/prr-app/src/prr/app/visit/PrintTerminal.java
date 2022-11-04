package prr.app.visit;

import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;
import prr.terminals.Terminal;
import prr.visitors.TerminalVisitor;

import static java.lang.Math.round;

public class PrintTerminal implements TerminalVisitor {

    private char _dash = '|';

    private String _line = null;

    private String _friends = null;

    @Override
    public void visitFancyTerminal(FancyTerminal terminal) {

        _line = getCharacteristics(terminal);

    }

    @Override
    public void visitBasicTerminal(BasicTerminal terminal) {

        _line = getCharacteristics(terminal);

    }

    private String getCharacteristics(Terminal terminal){

        return  terminal.getType() + _dash + terminal.getId() + _dash + terminal.getClientId() + _dash +
                terminal.getStateName() + _dash + round(terminal.getPayments()) + _dash +
                round(terminal.getDebt()) + getFriends(terminal);

    }
    private String getFriends(Terminal terminal){

        _friends = String.valueOf(_dash);
        terminal.getFriends().forEach(friend -> _friends += friend + ",");
        if(_friends != "|") {
            _friends = _friends.substring(0, (_friends.length() - 1));
        } else if (_friends == "|") {
            _friends = "";
        }
        return _friends;

    }

    @Override
    public String getLine(){
        return _line;
    }

}
