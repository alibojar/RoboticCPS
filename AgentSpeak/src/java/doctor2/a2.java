// Internal action code for project RoboticRescueDoctor

package doctor2;

import nxt.Communication;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class a2 extends DefaultInternalAction {
	
	Communication comm;
	Thread commThread;
	
	public a2(){
		comm = new Communication("Doctor2","00:16:53:11:13:f7");
		commThread = new Thread(comm);
		commThread.start();	
	}
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        StringTerm injLocation = (StringTerm) args[0];
        
    	comm.write(injLocation.getString());
    	String attended = comm.read();
    	
    	StringTerm attendedDone = new StringTermImpl(attended);
    	
        return un.unifies(attendedDone, args[1]);
    }
}
