/* The Player class. Created by client by filling out the options from 
 * PlayerCreateDisplay.
 * 
 * AUTHOR: Jonathan
 */

import java.util.Random;
import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L; // to appease the java gods
	private String name;
	private String tank;
	private String color;
	private String[] names = {":^)", ":(", ":)", ";~;", "o~o", "[uwu]"}; // default names

	// constructor. if the player didn't type in a new name, we give them a random 
	public Player(String name, String tank, String color) {
		if (name == "Default") {
			Random ran = new Random();
			this.name = names[ran.nextInt(6)]; 
			System.out.println("You didn't specify a name, so we're giving you a random one! It's " + this.name); }
		else { this.name = name; }
		
		this.tank = tank;
		this.color = color;
	}
	
	// getters
	public String getName() { return name; }
	public String getTank() { return tank; }
	public String getColor() { return color; }
}