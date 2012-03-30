import java.io.DataOutputStream;
import java.util.Queue;

import lejos.nxt.comm.NXTConnection;


public class BTSend implements Runnable{
	Queue<String> q;
	NXTConnection conn;
	DataOutputStream out;
	
	public BTSend(NXTConnection conn){
		q = new Queue<String>();
		this.conn=conn;
		out=conn.openDataOutputStream();
	}
	
	public void run(){
		try{
			while(true){
				while(q.empty()) Thread.yield();					
				out.writeUTF((String)q.pop());
				out.flush();
			}
		}
		catch (Exception e) {}
	}
	
	public void write(String message){
		q.push(message);
	}
}
