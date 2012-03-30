import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class Ultrasonic implements Runnable{
	UltrasonicSensor us;
	int distance;
	
	public Ultrasonic(){
		us= new UltrasonicSensor(SensorPort.S2);
		distance = us.getDistance();
	}
	
	public void run(){
		try{
			while(true){
				us.continuous();
				distance=us.getDistance();
				us.off();
				if (distance>=100) Thread.sleep(4000);
				else if (distance>=50) Thread.sleep(2000);
				else Thread.sleep(100);
			}
		}
		catch(Exception e){}
	}
	
	public int getDistance(){
		return distance;
	}
}
