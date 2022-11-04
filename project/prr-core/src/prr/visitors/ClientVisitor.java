package prr.visitors;

import prr.Client;


public interface ClientVisitor {

    void visit(Client client);

    String getLine();

}
