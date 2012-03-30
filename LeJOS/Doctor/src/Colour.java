import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;


public class Colour implements Runnable{
	ColorHTSensor cs;
	String colour;
	String[] colourName = {"Red", "Green", "Blue", "Yellow", "Navy", "Orange",
            "White", "Black", "Pink", "Gray", "Light gray", "Dark Gray", "Cyan"};
	public Colour(){
		cs = new ColorHTSensor(SensorPort.S4);
		colour = colourName[cs.getColorID()];
	}
	
	public void run(){
		try{
			while(true){
				colour = colourName[cs.getColorID()];
				Thread.sleep(25);
			}
		}
		catch(Exception e){}
	}
	
	public String getColour(){
		return colour;
	}
	
}
