import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L; // to appease the java gods
	private String name;
	private String tank;
	private String color;
	
	public Player(String name, String tank, String color) {
		this.name = name;
		this.tank = tank;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTank() {
		return tank;
	}
	
	public String getColor() {
		return color;
	}
}
