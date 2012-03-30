import java.io.DataInputStream;
import java.util.*;

import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.navigation.Navigator;


public class BTReceive implements Runnable{
	NXTConnection conn;
	DataInputStream in;
	Hashtable<Integer,Injured> table;
	Navigator robot;
	Thread doctorThead;
	
	public BTReceive(NXTConnection conn, Hashtable<Integer,Injured> table, Navigator robot, Thread doctorThread){
		this.doctorThead=doctorThread;
		this.conn=conn;
		this.table=table;
		this.robot=robot;
		in = conn.openDataInputStream();
	}
	
	public void run(){
		try{
			while(true) manipulate(in.readUTF());
		} catch (Exception e) {}
	}
	
	//Message scheme: Key;X;Y;Colour
	//Message example: 2;25.5;14.7;Red
	private void manipulate(String message) throws Exception{
		System.out.println(message);
		String[] injMessage = Split(message,";");
		Injured inj = new Injured();
		inj.setPoint(Float.parseFloat(injMessage[1]),Float.parseFloat(injMessage[2]));
		inj.setSeverity(injMessage[3]);
		table.put(Integer.parseInt(injMessage[0]),inj);
		robot.addWaypoint(inj.getPoint().x,inj.getPoint().y);
	}
	
	//Got from the internet, LeJOS does not have String.split()
	private String[] Split(String splitStr, String delimiter) {  
	    StringBuffer token = new StringBuffer();  
	    Vector<String> tokens = new Vector<String>();  
	    // split  
	    char[] chars = splitStr.toCharArray();  
	    for (int i=0; i < chars.length; i++) {  
	        if (delimiter.indexOf(chars[i]) != -1) {  
	            // we bumbed into a delimiter  
	            if (token.length() > 0) {  
	                tokens.addElement(token.toString());  
	                token.setLength(0);  
	            }  
	        } else {  
	            token.append(chars[i]);  
	        }  
	    }  
	    // don't forget the "tail"...  
	    if (token.length() > 0) {  
	        tokens.addElement(token.toString());  
	    }  
	    // convert the vector into an array  
	    String[] splitArray = new String[tokens.size()];  
	    for (int i=0; i < splitArray.length; i++) {  
	        splitArray[i] = tokens.elementAt(i);  
	    }  
	    return splitArray;  
	}
}
