/* The Player class. Created by client by filling out the options from 
 * PlayerCreateDisplay. Holds onto the player's tank as well as information
 * relative to it, plus finding the player's local bounds (resolution).
 * 
 * AUTHOR: Jonathan Houge
 */

import java.util.Random;
import java.io.Serializable;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Rectangle;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L; // to appease the java gods
	
	private String name;
	private String[] names = {":^)", ":(", ":)", ";~;", "o~o", "[uwu]"}; // default names
	private int id;
	
	private Tank tank;
	private int[] spawnRotation = {3, 5, 1, 7};
	
	// spawn points (x & y)
	private int[] spawnX = {50, 0, 50, 0};
	private int[] spawnY = {50, 50, 0, 0};
	
	private int x;
	private int y;
	
	// constructor. if the player didn't type in a new name, we give them a random 
	public Player(String name, String tank, String color, int id) {
		if (name.equals("Default")) { // randomly assign a name if none chosen
			Random ran = new Random();
			this.name = names[ran.nextInt(6)]; 
			System.out.println("You didn't specify a name, so we're giving you a random one! It's " + this.name); }
		else { this.name = name; }
		
		bounds(); // find the bounds
		
		// use the bounds to create tank starting location - pick the appropriate one based on player number (id)
		spawnX[1] = this.x - 100; spawnX[3] = this.x - 100;
		spawnY[2] = this.y - 150; spawnY[3] = this.y - 150;
		this.id = id;
		int startingX = spawnX[id - 1];
		int startingY = spawnY[id - 1];
		int startingRotation = spawnRotation[id-1];
		
		// create the right tank type
		if(tank.equals("Biggy")) {
			this.tank = new BigTank(startingX, startingY, color,startingRotation,this.name); }
		
		else if(tank.equals("Quicky")) {
			this.tank = new QuickTank(startingX, startingY, color,startingRotation,this.name); }
		
		else {
			this.tank = new DefaultTank(startingX, startingY, color,startingRotation,this.name); } }
	
	// find the player/client's bounds
	public void bounds() {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		Rectangle bounds = shell.getMonitor().getBounds();
		this.x = bounds.width; this.y = bounds.height;
		display.dispose(); }
	
	// string method - tells name, tank type, and location
	public String toString() {
		String builder = "Player[name,tankType,x,y]: [";
		builder += name + ',' + tank.getType() + ',' + x + ',' + y + ']';
		return builder; }
	
	// setters
	public void setDisplayWidth(int newX) { this.x = newX; }
	public void setDisplayHeight(int newY) { this.y = newY; }
	
	// getters
	public String getName() { return name; }
	public Tank getTank() { return tank; }
	public int getDisplayWidth() { return x; }
	public int getDisplayHeight() { return y; }
}