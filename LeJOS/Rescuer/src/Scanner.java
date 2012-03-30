import lejos.nxt.Motor;
import lejos.robotics.navigation.Navigator;


public class Scanner implements Runnable{
	Ultrasonic us;
	Touch ts;
	Thread tsThread, usThread;
	Navigator robot;
	
	Radar radar;
	Thread radarThread;
	
	boolean touchFlag,usFlag;
	
	public Scanner(Navigator robot){
		this.robot=robot;
		
		touchFlag = usFlag = true;
		
		us = new Ultrasonic();
		ts = new Touch();
		tsThread = new Thread(ts);
		usThread = new Thread(us);
		
		tsThread.setDaemon(true);
		tsThread.start();
		usThread.setDaemon(true);
		usThread.start();

		radar = new Radar();
		radarThread = new Thread(radar);
		radarThread.setDaemon(true);
		radarThread.start();
	}

	public void run(){
		try{
			while(true){
				if(ts.getTouch()){  
					touchFlag=false;
				}
				else if (us.getDistance()<=25){
					usFlag=false;
				}
				else { touchFlag = usFlag = true; }
			}
		}catch (Exception e){}
	}
	
	public boolean getTouchFlag(){
		return touchFlag;
	}
	
	public boolean getUsFlag(){
		return usFlag;
	}
}

class Radar implements Runnable{
	
	public void run(){
		try {
			startRadar();
		} catch (Exception e) {}
	}
	
	private void startRadar() throws Exception{
		final int ANGLE = 25;
		Motor.C.setSpeed(750);
		while(true){
			Motor.C.rotateTo(ANGLE);
			Motor.C.rotateTo(-ANGLE);
		}
	}
}

