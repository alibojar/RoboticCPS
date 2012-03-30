import lejos.nxt.Motor;
import lejos.robotics.navigation.Navigator;


public class Scanner implements Runnable{
	Ultrasonic us;
	//Touch ts;
	Thread tsThread, usThread;
	Navigator robot;
	
	Radar radar;
	Thread radarThread;
	
	boolean moveFlag;
	
	public Scanner(Navigator robot){
		this.robot=robot;
		
		moveFlag = true;
		
		us = new Ultrasonic();
		//ts = new Touch();
		//tsThread = new Thread(ts);
		usThread = new Thread(us);
		
		//tsThread.setDaemon(true);
		//tsThread.start();
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
				//if(us.getDistance() <=25 | ts.getTouch()){  
				if(us.getDistance() <=25){
					moveFlag=false;
					if (robot.isMoving()) robot.stop();
				}
				else { moveFlag=true; }
			}
		}
		catch(Exception e){}
	}
	
	//true to go, false to stop
	public boolean getMoveFlag(){
		return moveFlag;
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
		/*
		while(true){
			for(int i=-ANGLE; i<=ANGLE; i+=10){
				Motor.C.rotateTo(i);
				Thread.sleep(10);
			}
			
			for(int i=ANGLE; i>=-ANGLE; i-=10){
				Motor.C.rotateTo(i);
				Thread.sleep(10);
			}
			
		}
		*/
	}
}
