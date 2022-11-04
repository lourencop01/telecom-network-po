package prr.terminals;

import prr.Client;
import prr.visitors.TerminalVisitor;

public class BasicTerminal extends Terminal{

    private static final long serialVersionUID = 202208091753L; /*is it necessary?*/
    public BasicTerminal(String id, Client owner) {

        super(id, owner);

    }

    @Override
    public String getType(){
        return "BASIC";
    }

    @Override
    public void accept(TerminalVisitor visitor){
        visitor.visitBasicTerminal(this);
    }

}
