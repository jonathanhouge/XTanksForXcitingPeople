/* The Player class. Created by client by filling out the options from 
 * PlayerCreateDisplay.
 * 
 * AUTHOR: Jonathan
 */

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L; // to appease the java gods
	private String name;
	private Tank tank;
	private String[] names = {":^)", ":(", ":)", ";~;", "o~o", "[uwu]"}; // default names
	private int x;
	private int y;
	private int id;
	// constructor. if the player didn't type in a new name, we give them a random 
	public Player(String name, String tank, String color, int id) {
		if (name.equals("Default")) {
			Random ran = new Random();
			this.name = names[ran.nextInt(6)]; 
			System.out.println("You didn't specify a name, so we're giving you a random one! It's " + this.name); }
		else { this.name = name; }
		
		Display display = new Display();
		this.id = id;
		int startingX = id*100;
		int startingY = id*100;
		if(tank.equals("Biggy")) {
			this.tank = new BigTank(startingX,startingY,color);

		}else if(tank.equals("Quicky")) {
			this.tank = new QuickTank(startingX,startingY,color);
		}else {
			this.tank = new DefaultTank(startingX,startingY,color);

		}
			
		display.dispose();
	}
	
	public void bounds() {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		Rectangle bounds = shell.getMonitor().getBounds();
		this.x = bounds.height; this.y = bounds.width;
		display.dispose();
	}
	
	public String toString() {
		String builder = "Player[name,tankType,x,y]: [";
		builder += name + ',' + tank.getType() + ',' + x + ',' + y + ']';
		return builder;
	}
	// setters
	public void setDisplayWidth(int newX) { this.x = newX; }
	public void setDisplayHeight(int newY) { this.y = newY; }
	
	// getters
	public String getName() { return name; }
	public Tank getTank() { return tank; }
	public int getDisplayWidth() { return x; }
	public int getDisplayHeight() { return y; }
}