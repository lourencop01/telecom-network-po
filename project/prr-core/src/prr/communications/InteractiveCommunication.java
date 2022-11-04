package prr.communications;

import prr.terminals.Terminal;
import prr.visitors.CommunicationVisitor;

public abstract class InteractiveCommunication extends Communication{

        private double _duration;

        public InteractiveCommunication(Terminal from, Terminal to, int id){

            super(from, to, id, true);

        }

        public abstract String getType();

        public abstract void accept(CommunicationVisitor communicationVisitor);

        @Override
        public double getUnits(){
            return _duration;
        }

        public void setUnits(double minutes) {
            _duration = minutes;
        }
}

