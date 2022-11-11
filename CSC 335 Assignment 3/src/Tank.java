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
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;

public abstract class Tank implements Serializable {
	protected static final long serialVersionUID = 1L;
	protected int[] state; // [x,y,rotateState]
	protected int rotateState; // What rotate stage the tank is currently in
	protected transient Color color;
	protected transient Color armColor;
	protected int width;
	protected int height;
	protected int barrel;
	protected int[] xState;
	protected int[] yState;
	protected int health;
	protected transient List<Bullet> bulletList;
	
	/*
	 * This constructor sets the color of the tanks body, arm, and also starting position.
	 */
	public Tank(Color color, Color color2) {
		//this.state = new int[] { 300, 500, 0 };
		this.color = color;
		this.armColor = color2;
		this.bulletList = new ArrayList<>();
	} 
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
		for(Wall wall:walls) {
			if(wall.wall.contains(tempX, tempY)) {
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
		for(Wall wall:walls) {
			if(wall.wall.contains(tempX, tempY)) {
				return;
			}
		}
		state[1] = tempY;
		state[0] = tempX;

	}
	/*
	 * This method draws the tank onto the canvas. Because tanks can be rotated, it's necessary to 
	 * briefly translate the tank so that it rotates around itself rather than rotating around
	 * the origin of the canvas. 
	 */
	public void draw(GC gc) {
		Transform transform = new Transform(gc.getDevice());
		transform.translate(state[0], state[1]);
		transform.rotate(45 * rotateState);
		gc.setTransform(transform);
		gc.setBackground(color);
		gc.fillRectangle(-width / 2, -height / 2, width, height);
		gc.setBackground(armColor);
		gc.fillOval(-width / 2, -height / 4, width, width); 
		gc.setLineWidth(4);
		gc.drawLine(0, 0, 0, barrel);
		transform = new Transform(gc.getDevice()); 	// for some reason this line fixes bug where shapes get
													// drawn based on tank origin instead of canvas origin
		gc.setTransform(transform);
	}
	/*
	 * This method is responsible for drawing a specific tanks bullets that they have shot.
	 * It calls the bullet's draw() method and then checks to see if the bullet is still within
	 * bounds, destroying it if it is out of the map or TODO hits a wall. 
	 */
	public void drawBullets(GC gc) {
		for(int i = 0; i < bulletList.size();i++) {
			bulletList.get(i).draw(gc);
			if(!bulletList.get(i).withinBounds(gc.getDevice().getBounds())) {
				bulletList.remove(i);
				i--;
			}
		}
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
	public abstract void shoot();
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

	public DefaultTank(Color color, Color color2) {
		super(color, color2);
		this.state = new int[] {300,500,0};
		this.xState = new int[]{ 0, 7, 10, 7, 0, -7, -10, -7 };
		this.yState = new int[]{ -10, -7, 0, 7, 10, 7, 0, -7 };
		this.health = 4;
		this.width = 25;
		this.height = 50;
		this.barrel = (int) -(height * (.75));
	}

	@Override
	public String getType() {
		return "Defaulty";
	}

	@Override
	public void shoot() {
		int rotateMult = 3;
		int xOffset = this.xState[rotateState]*rotateMult;
		int yOffset = this.yState[rotateState]*rotateMult;
		bulletList.add(new DefaultBullet(color, this.state[0]+xOffset,this.state[1]+yOffset,
				this.xState[rotateState],this.yState[rotateState]));
	
	}
	
}
class QuickTank extends Tank implements Serializable{
	private static final long serialVersionUID = 1L;
	public QuickTank(Color color,Color color2){
		super(color,color2);
		this.state = new int[] {300,500,0};
		this.xState = new int[]{ 0, 9, 13, 9, 0, -9, -13, -9 };
		this.yState = new int[]{ -13, -9, 0, 9, 13, 9, 0, -9 };
		this.health = 3;
		this.width = 25;
		this.height = 30;
		this.barrel = (int) -(height * (.75));
	}
	@Override
	public String getType() {
		return "Quicky";
	}
	/*
	 * This method 'shoots' a bullet by adding a bullet to the tanks 
	 * bulletList so that the bullet may be drawn and possibly interact 
	 * with other enemy tanks in the future.
	 */
	@Override
	public void shoot() {
		int rotateMult = 1;
		int xOffset = this.xState[rotateState]*rotateMult;
		int yOffset = this.yState[rotateState]*rotateMult;
		bulletList.add(new QuickBullet(color, this.state[0]+xOffset,this.state[1]+yOffset,
				this.xState[rotateState],this.yState[rotateState]));
	}
}
class BigTank extends Tank implements Serializable{

	private static final long serialVersionUID = 1L;

	public BigTank(Color color, Color color2) {
		super(color, color2);
		this.state = new int[] {800,500,2};
		this.xState = new int[] { 0, 5, 7, 5, 0, -5, -7, -5 };
		this.yState = new int[ ]{ -7, -5, 0, 5, 7, 5, 0, -5 };
		this.health = 6;
		this.width = 40;
		this.height = 55;
		this.barrel = (int) -(height * (.75));
	}

	@Override
	public String getType() {
		return "Biggy";
	}

	@Override
	public void shoot() {
		int rotateMult = 6;
		int xOffset = this.xState[rotateState]*rotateMult;
		int yOffset = this.yState[rotateState]*rotateMult;
		bulletList.add(new BigBullet(color, this.state[0]+xOffset,this.state[1]+yOffset,
		this.xState[rotateState],this.yState[rotateState]));
	}
	
}