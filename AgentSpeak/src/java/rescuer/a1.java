package rescuer;

import nxt.Communication;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class a1 extends DefaultInternalAction {
	
	Communication comm;
	Thread commThread;
	
	public a1(){
		comm = new Communication("Rescuer","00:16:53:12:ad:91");
		commThread = new Thread(comm);
		commThread.start();	
	}
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

    	String message=comm.read();   	
        
        StringTerm result = new StringTermImpl(message);
        
        return un.unifies(result, args[0]);   
    }
}