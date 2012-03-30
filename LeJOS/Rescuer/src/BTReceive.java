import java.io.DataInputStream;
import java.util.*;

import lejos.nxt.comm.NXTConnection;


public class BTReceive implements Runnable{
	Queue<String> q;
	NXTConnection conn;
	DataInputStream in;
	
	public BTReceive(NXTConnection conn){
		q = new Queue<String>();
		this.conn=conn;
		in = conn.openDataInputStream();
	}
	
	public void run(){
		try{
			while(true){
				q.push(in.readUTF());
			}
		}
		catch (Exception e) {}
	}
	
	public String read(){
		String message=null;
		while(q.empty()) Thread.yield();
		message=(String) q.pop();
		return message;
	}
}
