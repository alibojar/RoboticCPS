package nxt;

import lejos.pc.comm.*;

import java.io.*;
import java.util.concurrent.*;


public class Communication implements Runnable{
	
	
	NXTConnector conn;
	DataOutputStream dos;
	DataInputStream dis;
	
	String received;
	LinkedBlockingQueue<String> q;
	
	 public Communication(String nxtName, String nxtBTAddress){

		 received=null;
		 q = new LinkedBlockingQueue<String>();
		 
		 conn = new NXTConnector();
		 
		 boolean connected = conn.connectTo(nxtName,nxtBTAddress,2);
		 
		 if(!connected){
				System.err.println("Failed to connect!");
				System.exit(1);
		 }
		 
		 System.out.println("Connected to " + nxtName);
		 
		 dis = new DataInputStream(conn.getInputStream());
		 dos = new DataOutputStream(conn.getOutputStream());
		 
	 }
	 
	 public void run(){
		 try{
			 while(true){
				q.put(dis.readUTF());
				Thread.sleep(100);
			 }
		 } catch(Exception e){}  
	 }
	 
	 public String read(){
		 while(q.size()==0) Thread.yield();
		 return q.poll();	 
	 }
	 
	 public void write(String message) throws Exception{
		 dos.writeUTF(message);
		 dos.flush();
		 Thread.sleep(100);
	 }
	 
	 

}

