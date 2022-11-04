package prr.visitors;

import prr.terminals.BasicTerminal;
import prr.terminals.FancyTerminal;

public interface TerminalVisitor {

    void visitFancyTerminal(FancyTerminal terminal);

    void visitBasicTerminal(BasicTerminal terminal);

    String getLine();

}
