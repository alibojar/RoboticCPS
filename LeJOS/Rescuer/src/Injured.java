import lejos.geom.Point;


public class Injured {
	private Point point;
	String severity;
	
	public Injured(){
		point = new Point(0,0);
		severity=null;
	}
	
	public Point getPoint(){
		return point;
	}
	
	public void setPoint(Point point){
		this.point=point;
	}
	
	
	public String getSeverity(){
		return severity;
	}
	
	public void setSeverity(String severity){
		this.severity = severity;
	}

}
