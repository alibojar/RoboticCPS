// Internal action code for project RoboticRescueDoctor

package robot;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class severity extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	int severityLevel=0;
    	
        StringTerm inj = (StringTerm) args[0];
		
		try{
			String colour=inj.toString().split(";")[3].replace('"', ' ').trim();
			if (colour.equals("Red")) severityLevel=3;
	        else if (colour.equals("Navy")) severityLevel=2;
	        else if (colour.equals("Green")) severityLevel=1;
			
		} catch (Exception e) {
			System.out.println("Error:");
			System.out.println(e.getMessage());
		}
        
        NumberTerm result = new NumberTermImpl(severityLevel);
        
        return un.unifies(result, args[1]);
    }
}
