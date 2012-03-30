import lejos.nxt.*;

public class Touch implements Runnable{
	TouchSensor ts;
	boolean touch;
	
	public Touch(){
		ts = new TouchSensor(SensorPort.S3);
		touch = ts.isPressed();
	}
	
	public void run(){
		try{
			while(true){
				touch = ts.isPressed();
				Thread.sleep(50);
			}
		}
		catch(Exception e){}
	}
	
	public boolean getTouch(){
		return touch;
	}
}
