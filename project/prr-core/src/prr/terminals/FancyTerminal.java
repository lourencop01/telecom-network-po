package prr.terminals;

import prr.Client;
import prr.visitors.TerminalVisitor;

public class FancyTerminal extends Terminal{

    private static final long serialVersionUID = 202208091753L; /*is it necessary?*/

    public FancyTerminal(String id, Client owner) {

        super(id, owner);

    }

    @Override
    public String getType(){
        return "FANCY";
    }

    @Override
    public void accept(TerminalVisitor visitor){

        visitor.visitFancyTerminal(this);

    }

}
