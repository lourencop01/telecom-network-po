package prr.communications;

import prr.terminals.Terminal;
import prr.visitors.CommunicationVisitor;

import java.io.Serializable;

public abstract class Communication implements Serializable {

    private static final long serialVersionUID = 202225102258L;

    private int _id;

    private double _price;

    private Terminal _from;

    private Terminal _to;

    private boolean _ongoing;

    private boolean _isPaid;

    public Communication(Terminal from, Terminal to, int id, boolean ongoing){

        _from = from;
        _to = to;
        _id = id;
        _ongoing = ongoing;

        _price = 0;
        _isPaid = false;
    }

    public abstract void accept(CommunicationVisitor communicationVisitor);

    public abstract String getType();

    public abstract double getUnits();

    public String fromTerminalId() {
        return _from.getId();
    }

    public String fromClientId() {
        return _from.getClientId();
    }

    public String toTerminalId() {
        return _to.getId();
    }

    public String toClientId() {
        return _to.getClientId();
    }

    public int getId() {
        return _id;
    }

    public double getPrice(){
        return _price;
    }

    public void setPaid(){
        _isPaid = true;
    }
    public boolean isPaid(){
        return _isPaid;
    }

    public void setPrice(double price) {
        _price = price;
    }

    public boolean getStatus(){
        return _ongoing;
    }

    public void setStatus(boolean value) {
        _ongoing = value;
    }

}
