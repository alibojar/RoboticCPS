import java.util.*;

import lejos.nxt.*;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.localization.*;
import lejos.robotics.navigation.*;

public class Rescuer implements NavigationListener{
	
	static DifferentialPilot  pilot;
	static OdometryPoseProvider pp;
	static Navigator robot;
	
	static Scanner scanner;
	static Thread scannerThread;
	
	static Colour c;
	static Thread colourThread;
	
	static Injured inj;
	static Hashtable<Integer,Injured> table;
	
	static NXTConnection conn;
	static BTSend sender;
	static BTReceive receiver;
	static Thread senderThread;
	static Thread receiverThread;
	
	static NavigationListener nLis;
	
	public static void main(String[] args) throws Exception {
		Button.ESCAPE.addButtonListener( new ButtonListener() {
			public void buttonPressed(Button b) {
				System.exit(1);
			}
			public void buttonReleased(Button b) {
				
			}			
		});
		
		table = new Hashtable<Integer,Injured>();
		pilot = new DifferentialPilot(3.25f,19.8f,Motor.B, Motor.A); //Updated 13.02.2012
		pp= new OdometryPoseProvider(pilot);
		pp.setPose(new Pose(20,20,0));
		robot = new Navigator(pilot,pp);				
				
		nLis = new Rescuer();
		robot.addNavigationListener(nLis);
		
		inj = new Injured();
		
		System.out.println("Waiting");
		conn = Bluetooth.waitForConnection();
		System.out.println("Connected");
		
		sender = new BTSend(conn);
		receiver = new BTReceive(conn);
		senderThread = new Thread(sender);
		receiverThread = new Thread(receiver);
		senderThread.setDaemon(true);
		senderThread.start();
		receiverThread.setDaemon(true);
		receiverThread.start();
		
		c = new Colour();
		colourThread = new Thread(c);
		colourThread.setDaemon(true);
		colourThread.start();
		
		scanner = new Scanner(robot);
		scannerThread = new Thread(scanner);
		scannerThread.setDaemon(true);
		scannerThread.start();
		
		a1();
		Button.waitForAnyPress();

	}
	
	public static void a1() throws Exception{
		
		String currentColour= c.getColour();
		boolean sameInjured=true;
		int key=0;
		robot.addWaypoint(170, 30);
		robot.addWaypoint(170, 210);
		robot.addWaypoint(50, 200);
		robot.addWaypoint(160, 150);
		robot.addWaypoint(170, 220);
		robot.followPath();
		
		try{
			while(true){
				//robot.getMoveController().forward();
				
				if (scanner.getTouchFlag() & scanner.getUsFlag()){
					currentColour=c.getColour();
					if (currentColour.equals("Pink")) sameInjured=true;
					//else if (!currentColour.equals("Pink") && sameInjured){
					else if ((currentColour.equals("Red") || currentColour.equals("Navy")) && sameInjured){
						sameInjured=false;
						Sound.beepSequenceUp();
						inj.setPoint(pp.getPose().getLocation());
						inj.setSeverity(currentColour);
						key++;
						table.put(key, inj);
						sendInfo(table,key);
					}
				}
				else if (!scanner.getTouchFlag()){
					robot.stop();
					robot.getMoveController().travel(-10);
					pilot.rotate(randomRotate());
				}
				else if (!scanner.getUsFlag()){
					robot.stop();
					Thread.sleep(4000);
					pilot.rotate(randomRotate());
				}
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			Thread.sleep(1000);
			System.exit(1);
		}
	}
	
	public static void a3() throws Exception{
		
	}
	
	
	public static void sendInfo(Hashtable<Integer,Injured> table, int key) throws InterruptedException{
		Injured inj = (Injured) table.get(key);
		String msg = key + ";" 
					+ Math.round(inj.getPoint().x) + ";" 
					+ Math.round(inj.getPoint().y) + ";"
					+ inj.getSeverity();
		sender.write(msg);	
	}

	
	public void atWaypoint(Waypoint waypoint, Pose pose, int sequence) {
		
	}

	
	public void pathComplete(Waypoint waypoint, Pose pose, int sequence) {
		sender.write("end");
		Thread.yield();
	}

	
	public void pathInterrupted(Waypoint waypoint, Pose pose, int sequence) {
	}
	
	public static int randomRotate(){
		double random = Math.random();
		int angle;
		if (Math.round(random)>0) angle = (int)Math.round((random * -90)-90);
		else angle = (int)Math.round((random * 90)+90);
		return angle;
	}
	
	public static float updateX(){
		float x= pp.getPose().getX();
		float newX =x;
		if (x>=35 && x<=65) newX=50;
		else if (x>=135 && x<=165) newX=150;
		sender.write("newX:"+newX);
		return newX;
	}
	
	public static float updateY(){
		
		float y= pp.getPose().getY();
		float newY=y;
		if (y>=20 && y<=50) newY=35;
		else if (y>=105 && y<=135) newY=120;
		else if (y>=190 && y<=220) newY=205;
		sender.write("newY:"+newY);
		return newY;
	}
	
	/* This is the code that I used to test the colour strips
	  	
	  	robot.rotateTo(45);
		while(robot.isMoving()) Thread.yield();
		float x,y,h;
		while(true){
			if (scanner.getTouchFlag() & scanner.getUsFlag()){
				robot.getMoveController().forward();
				if (c.getColour().equals("Black")){
					sender.write(pp.getPose().toString());
					x=pp.getPose().getX();
					y=updateY();
					h = pp.getPose().getHeading();
					robot.getPoseProvider().setPose(new Pose(x,y,h));
					sender.write(robot.getPoseProvider().getPose().toString());
					sender.write("==========");
				}
				else if (c.getColour().equals("Blue")){
					sender.write(pp.getPose().toString());
					x=updateX();
					y=pp.getPose().getY();
					h = pp.getPose().getHeading();
					robot.getPoseProvider().setPose(new Pose(x,y,h));
					sender.write(robot.getPoseProvider().getPose().toString());
					sender.write("==========");
					break;
				}
			}
			else if (!scanner.getTouchFlag()){
				robot.stop();
				robot.getMoveController().travel(-10);
				pilot.rotate(randomRotate());
			}
			else if (!scanner.getUsFlag()){
				robot.stop();
				Thread.sleep(4000);
				pilot.rotate(randomRotate());
			}
		}
		
	 */
}
