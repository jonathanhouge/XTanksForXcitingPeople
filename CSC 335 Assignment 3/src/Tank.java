/*
* AUTHOR: Julius Ramirez
* FILE: Tank.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to create an abstract class that defines what subclasses of tank can do.
* Subclasses of tank include: Defaulty (DefaultTank), Quicky (QuickTank), and Biggy (BigTank).
*
*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

public abstract class Tank implements Serializable {
	protected static final long serialVersionUID = 1L;
	protected int[] state; // [x,y,rotateState]
	protected int rotateState; // What rotate stage the tank is currently in
	protected String color;
	protected String armColor;
	protected int width;
	protected int height;
	protected int barrel;
	protected int[] xState;
	protected int[] yState;
	protected int health;
	protected List<Bullet> bulletList;
	protected Rectangle base;
	protected int rotateMult;
	/*
	 * This constructor sets the color of the tanks body, arm, and also starting position.
	 */
	public Tank(String color, String color2) {
		//this.state = new int[] { 300, 500, 0 };
		this.color = color;
		this.armColor = color2;
		this.bulletList = new ArrayList<>();
	} 
	public String toString() {
		String builder = Arrays.toString(state);
		return builder;
	}
	/*
	 * This method draws the tank onto the canvas. Because tanks can be rotated, it's necessary to 
	 * briefly translate the tank so that it rotates around itself rather than rotating around
	 * the origin of the canvas. 
	 */
	public void draw(GC gc) {
		Transform transform = new Transform(gc.getDevice());
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY));
		gc.fillRectangle(base); 									//draw base before translate
		transform.translate(state[0] + base.width/2, state[1]+base.height/2);
		transform.rotate(45 * rotateState);
		gc.setTransform(transform);
		gc.setBackground(getColor(gc,color));
		gc.fillRectangle(-width / 2, -height / 2, width, height);	//draw top
		gc.setBackground(getColor(gc,armColor));
		gc.fillOval(-width / 2, -height / 4, width, width); 		//draw circle
		gc.setLineWidth(4);
		gc.drawLine(0, 0, 0, barrel);								//draw barrel
		transform = new Transform(gc.getDevice()); 	// for some reason this line fixes bug where shapes get
													// drawn based on tank origin instead of canvas origin
		gc.setTransform(transform);
	}
	public Color getColor(GC gc, String want) {
		Color color;
		if (want.equals("Red")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_RED); }
		else if (want.equals("Blue")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_BLUE); }
		else if (want.equals("Black")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_BLACK); }
		else if (want.equals("Gray")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY); }
		else { // green [default]
			color = gc.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN); }
		
		return color; }
	/*
	 * This method increases the rotate state by one then checks if the number is
	 * bounded correctly
	 */
	public void turnRight() {
		rotateState++;
		checkRotateState();
	}
	/*
	 * This method decreases the rotate state by one then checks if the number is
	 * bounded correctly
	 */
	public void turnLeft() {
		rotateState--;
		checkRotateState();
	}
	/*
	 * This method ensures that the rotate state is within bounds, setting the value
	 * to either zero or one less than the size of however many states the tank has.
	 */
	protected void checkRotateState() {
		if (rotateState <= -1) {
			rotateState = yState.length - 1;
		} else if (rotateState >= yState.length) {
			rotateState = 0;
		}
	}
	
	/*
	 * This method updates the X and Y values of the tank by adding the proper
	 * values based on the rotate state of the tank
	 */
	public void moveForward(List<Wall> walls) {
		int tempX = state[0] + xState[rotateState] ;
		int tempY = state[1] + yState[rotateState];
		int oldX = this.base.x;
		int oldY = this.base.y;
		this.base.x = tempX;
		this.base.y = tempY;
		for(Wall wall:walls) {
			if(wall.wall.intersects(base)) {
				this.base.x = oldX;
				this.base.y = oldY;
				return;
			}
		}
		state[1] = tempY;
		state[0] = tempX;
		
	}
	/*
	 * This method updates the X and Y values of the tank by subtracting the proper
	 * values based on the rotate state of the tank
	 */
	public void moveBackward(List<Wall> walls) {
		int tempX = state[0] - xState[rotateState] ;
		int tempY = state[1] - yState[rotateState];
		int oldX = this.base.x;
		int oldY = this.base.y;
		this.base.x = tempX;
		this.base.y = tempY;
		for(Wall wall:walls) {
			if(wall.wall.intersects(base)) {
				this.base.x = oldX;
				this.base.y = oldY;
				return;
			}
		}
		state[1] = tempY;
		state[0] = tempX;
		
	}
	
	/*
	 * This method is responsible for drawing a specific tanks bullets that they have shot.
	 * It calls the bullet's draw() method and then checks to see if the bullet is still within
	 * bounds, destroying it if it is out of the map or TODO hits a wall. 
	 */
	public void drawBullets(GC gc,List<Wall> walls, Player[] players) {
		for(int i = 0; i < bulletList.size();i++) {
			boolean hasCollided = false;
			bulletList.get(i).draw(gc,getColor(gc,"Black"));
			for(Wall wall: walls) {
				if(wall.wall.contains(bulletList.get(i).coordinates[0], bulletList.get(i).coordinates[1])) {
					System.out.println("Bullet has touched a wall! Deleting bullet");
					bulletList.remove(i);
					i--;
					hasCollided = true;
					break;
				}
			}
			if(!hasCollided) {	// If the bullet has already collided with a wall, this code can be skipped
				for(Player player:players) {
					if(player !=null) {
						if(bulletList.get(i).hasHit(player.getTank().base)) {
							System.out.println("Bullet has touched a tank! Deleting bullet");
							bulletList.remove(i);
							i--;
							player.getTank().gotHit();
							break;
						}
					}
					
				}
			}
			
			
		}
	}
	private void gotHit() {
			this.health--;
	}
	
	public boolean stillAlive() {
		return this.health > 0;
	}
	/*
	 * This method simply returns what type of tank the player is using. It is 
	 * implemented in the subclasses.
	 */
	public abstract String getType();
	/*
	 * This method 'shoots' a bullet by adding a bullet to the tanks 
	 * bulletList so that the bullet may be drawn and possibly interact 
	 * with other enemy tanks in the future.
	 */
	public void shoot() {
		int xOffset = this.xState[rotateState]*rotateMult;
		int yOffset = this.yState[rotateState]*rotateMult;
		bulletList.add(new DefaultBullet(this.state[0]+this.base.width/2+xOffset,this.state[1]+this.base.height/2+yOffset,
				this.xState[rotateState],this.yState[rotateState]));
	}
	public boolean hasHit(Rectangle rect) { //TODO implement method
        for(Bullet bullet:bulletList) {
            if (bullet.hasHit(rect)) {
                return true;
            }
        }
        return false;
    }
}
class DefaultTank extends Tank implements Serializable{
	private static final long serialVersionUID = 1L;

	public DefaultTank(String color, String color2) {
		super(color, color2);
		this.state = new int[] {300,500,0};
		this.xState = new int[]{ 0, 7, 10, 7, 0, -7, -10, -7 };
		this.yState = new int[]{ -10, -7, 0, 7, 10, 7, 0, -7 };
		this.health = 4;
		this.width = 25;
		this.height = 50;
		this.barrel = (int) -(height * (.75));
		this.base = new Rectangle(300,500,55,55);
		this.rotateMult = 3;
	}

	@Override
	public String getType() {
		return "Defaulty";
	}
	
}
class QuickTank extends Tank implements Serializable{
	private static final long serialVersionUID = 1L;
	public QuickTank(String color, String color2){
		super(color, color2);
		this.state = new int[] {300,500,0};
		this.xState = new int[]{ 0, 9, 13, 9, 0, -9, -13, -9 };
		this.yState = new int[]{ -13, -9, 0, 9, 13, 9, 0, -9 };
		this.health = 3;
		this.width = 25;
		this.height = 30;
		this.barrel = (int) -(height * (.75));
		this.base = new Rectangle(300,500,35,35);
		this.rotateMult = 1;
	}
	@Override
	public String getType() {
		return "Quicky";
	}
}
class BigTank extends Tank implements Serializable{

	private static final long serialVersionUID = 1L;

	public BigTank(String color, String color2) {
		super(color, color2);
		this.state = new int[] {800,500,2};
		this.xState = new int[] { 0, 5, 7, 5, 0, -5, -7, -5 };
		this.yState = new int[ ]{ -7, -5, 0, 5, 7, 5, 0, -5 };
		this.health = 6;
		this.width = 40;
		this.height = 55;
		this.barrel = (int) -(height * (.75));
		this.base = new Rectangle(800,500,60,60);
		this.rotateMult = 6;
	}

	@Override
	public String getType() {
		return "Biggy";
	}
	
}