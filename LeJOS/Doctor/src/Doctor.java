import java.util.*;

import javax.bluetooth.LocalDevice;

import lejos.nxt.*;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.localization.*;
import lejos.robotics.navigation.*;

public class Doctor implements NavigationListener{
	
	static int key;
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
		Thread.currentThread().setName("doctorThread");
		
		Button.ESCAPE.addButtonListener( new ButtonListener() {
			public void buttonPressed(Button b) {
				System.exit(1);
			}
			public void buttonReleased(Button b) {
				
			}			
		});
		
		key=0;
		
		table = new Hashtable<Integer,Injured>();
		pilot = new DifferentialPilot(3.25f,19.8f,Motor.B, Motor.A); //Updated 13.02.2012
		pp= new OdometryPoseProvider(pilot);
		if (LocalDevice.getLocalDevice().getFriendlyName().equals("Doctor1")) pp.setPose(new Pose(20,100,0));
		else if (LocalDevice.getLocalDevice().getFriendlyName().equals("Doctor2")) pp.setPose(new Pose(20,200,0));
		
		robot = new Navigator(pilot,pp);
		
		nLis = new Doctor();
		robot.addNavigationListener(nLis);
		
		inj = new Injured();
		
		System.out.println("Waiting");
		conn = Bluetooth.waitForConnection();
		System.out.println("Connected");
		
		sender = new BTSend(conn);
		receiver = new BTReceive(conn,table,robot,Thread.currentThread());
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
		
		a2();
		Button.waitForAnyPress();
		
	}
	
	public static void a2() throws Exception{ 
		while(true){
			if (robot.getPath().isEmpty() || robot.isMoving()) Thread.yield();
			else if (!robot.pathCompleted()) robot.followPath();
			Thread.sleep(1000);
		}
	}
	
	
	public void atWaypoint(Waypoint waypoint, Pose pose, int sequence) {
		try{
			Sound.beepSequence();
			Thread.sleep(8000);
			sender.write("done");
		} catch (InterruptedException e) {}
	}

	
	public void pathComplete(Waypoint waypoint, Pose pose, int sequence) {
		Sound.beepSequenceUp();	
	}

	
	public void pathInterrupted(Waypoint waypoint, Pose pose, int sequence){
		try {
			Sound.buzz();
			Thread.sleep(4000);
			robot.goTo(waypoint);
		} catch (Exception e) {}
	}
}
