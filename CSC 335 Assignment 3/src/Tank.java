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
	protected int[] state; 		// [x,y,rotateState]
	protected int[] defaultState;
	protected String color;
	protected String armColor;
	protected int width;
	protected int height;
	protected int barrel;
	protected int[] xState; 	// moving forward based on x/yState[state[2]]
	protected int[] yState;
	protected int health;
	protected int maxHealth;
	protected List<Bullet> bulletList; // list of active bullets
	protected Rectangle base;
	protected int rotateMult;
	protected int bulletSize;
	protected String name;
	/*
	 * This constructor sets the color of the tanks body, arm, and also starting
	 * position.
	 */
	public Tank(int x,int y, String tankColor,String name) { //TODO unique tank spawn locations based on playerID.
		this.color = tankColor;
		this.armColor = "Black";
		this.bulletList = new ArrayList<>();
		this.name = name;
	}

	/*
	 * This method is more of a debugging tool, it prints the data within the state array,
	 * letting the user know the x/y and state[2] of the tank
	 */
	public String toString() {
		String builder = Arrays.toString(state);
		return builder;
	}

	/*
	 * This method draws the tank onto the canvas. Because tanks can be rotated,
	 * it's necessary to briefly translate the tank so that it rotates around itself
	 * rather than rotating around the origin of the canvas.
	 */
	public void draw(GC gc) {
		if(this.isAlive()) {
			System.out.println(name);
			Transform transform = new Transform(gc.getDevice());
			gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
			gc.fillRectangle(base); // draw base before translate
			if(state[2]!= 0) {
				gc.drawString(name,state[0], state[1] - base.height/4 - 11,true); // draw text
			}else {
				gc.drawString(name,state[0], state[1] + base.height + 6,true); // draw text
			}
			transform.translate(state[0] + base.width / 2, state[1] + base.height / 2);
			transform.rotate(45 * state[2]);
			gc.setTransform(transform);
			gc.setBackground(getColor(gc, color));
			gc.fillRectangle(-width / 2, -height / 2, width, height);// draw top base
			gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
			gc.fillOval(-width / 2, -height / 4, width, width); // draw circle
			gc.setLineWidth(4);
			gc.drawLine(0, 0, 0, barrel); // draw barrel
			transform = new Transform(gc.getDevice()); // reset transform (is this the best way though?)
			gc.setTransform(transform);
		}
		else {
			this.state[0] = -100;
			this.state[1] = -100;
			this.base.x = -100;
			this.base.y = -100;
		}
	}

	/*
	 * This method returns a Color object due to the Color class being
	 * non-serializable
	 */
	private Color getColor(GC gc, String want) {
		Color color;
		if (want.equals("Red")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_RED);
		} else if (want.equals("Blue")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_BLUE);
		} else if (want.equals("Yellow")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_YELLOW);
		} else if (want.equals("Gray")) {
			color = gc.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY);
		} else { // green [default]
			color = gc.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN);
		}

		return color;
	}

	/*
	 * This method increases the rotate state by one then checks if the number is
	 * bounded correctly
	 */
	public void turnRight() {
		state[2]++;
		checkRotateState();
	}

	/*
	 * This method decreases the rotate state by one then checks if the number is
	 * bounded correctly
	 */
	public void turnLeft() {
		state[2]--;
		checkRotateState();
	}

	/*
	 * This method ensures that the rotate state is within bounds, setting the value
	 * to either zero or one less than the size of however many states the tank has.
	 */
	protected void checkRotateState() {
		if (state[2] <= -1) {
			state[2] = yState.length - 1;
		} else if (state[2] >= yState.length) {
			state[2] = 0;
		}
	}

	/*
	 * This method updates the X and Y values of the tank by adding the proper
	 * values based on the rotate state of the tank if the move is valid.
	 */
	public void moveForward(List<Wall> walls, Player[] players) {
		int tempX = state[0] + xState[state[2]];
		int tempY = state[1] + yState[state[2]];
		if (moveIsValid(walls, players, tempX, tempY)) {
			state[1] = tempY;
			state[0] = tempX;
		}

	}
	
	/*
	 * This method updates the X and Y values of the tank by subtracting the proper
	 * values based on the rotate state of the tank if the move is valid.
	 */
	public void moveBackward(List<Wall> walls, Player[] players) {
		int tempX = state[0] - xState[state[2]];
		int tempY = state[1] - yState[state[2]];
		if (moveIsValid(walls, players, tempX, tempY)) {
			state[1] = tempY;
			state[0] = tempX;
		}
	}
	
	/*
	 * This private method is used to determine if the tank will actually move or not,
	 * utilizing the list of all walls on the map and the list of players so that
	 * this tank doesn't intersect with another tank. It returns true if the space 
	 * can be moved to, false if something is in the way. The base is shifted to 
	 * determine this calculation and is reset to its previous position if the spot
	 * is not occupied.
	 */
	private boolean moveIsValid(List<Wall> walls, Player[] players, int x, int y) {
		int oldX = this.base.x;
		int oldY = this.base.y;
		this.base.x = x;	// Temporarily shift base
		this.base.y = y;
		for (Wall wall : walls) {
			if (wall.getWall().intersects(base)) {
				this.base.x = oldX;	//Reset base
				this.base.y = oldY;
				System.out.println("Intersect with wall!");
				return false;
			}
		}
		for (Player player : players) {
			if (player != null) {
				if (!player.getTank().equals(this)) { // This prevents self collision
					if (this.base.intersects(player.getTank().base)) {
						this.base.x = oldX;
						this.base.y = oldY;
						System.out.println("Bases intersect!");
						return false;
					}
				}

			}
		}
		return true;
	}

	/*
	 * This method is responsible for drawing a specific tanks bullets that they
	 * have shot. It calls the bullet's draw() method and then checks to see if the
	 * bullet is still within bounds, destroying it if it hits a wall or a player
	 */
	public void drawBullets(GC gc, List<Wall> walls, Player[] players) {
		for (int i = 0; i < bulletList.size(); i++) {
			boolean hasCollided = false;
			bulletList.get(i).draw(gc, getColor(gc, color));
			for (Wall wall : walls) {
				if (wall.getWall().contains(bulletList.get(i).coordinates[0], bulletList.get(i).coordinates[1])) {
					System.out.println("Bullet has touched a wall! Deleting bullet");
					bulletList.remove(i);
					i--;
					hasCollided = true;
					break;
				}
			}
			if (!hasCollided) { // If the bullet has already collided with a wall, this block can be skipped
				for (Player player : players) {
					if (player != null) {
						if (bulletList.get(i).hasHit(player.getTank().base)) {
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

	/*
	 * This method decreases a tanks health due to it being hit by a bullet
	 */
	private void gotHit() {
		this.health--;
	}

	/*
	 * This method simply returns whether or not the health is above zero.
	 */
	public boolean isAlive() {
		return this.health > 0;
	}

	/*
	 * This method simply returns what type of tank the player is using. It is
	 * implemented in the subclasses.
	 */
	public abstract String getType();

	/*
	 * This method 'shoots' a bullet by adding a bullet to the tanks bulletList so
	 * that the bullet may be drawn and possibly interact with other enemy tanks in
	 * the future.
	 */
	public void shoot() {
		int xOffset = this.xState[state[2]] * rotateMult;
		int yOffset = this.yState[state[2]] * rotateMult;
		bulletList.add(new Bullet(this.state[0] + this.base.width / 2 + xOffset,
				this.state[1] + this.base.height / 2 + yOffset, this.xState[state[2]], this.yState[state[2]],
				bulletSize));
	}

	protected abstract void reset();
	
}

class DefaultTank extends Tank implements Serializable {
	private static final long serialVersionUID = 1L;

	public DefaultTank(int x,int y, String tankColor,int rotateState,String name) {
		super(x,y,tankColor,name);
		this.state = new int[] { x, y, rotateState };
		this.defaultState = new int[] { x, y, rotateState };
		this.xState = new int[] { 0, 7, 10, 7, 0, -7, -10, -7 };
		this.yState = new int[] { -10, -7, 0, 7, 10, 7, 0, -7 };
		this.health = 4;
		this.maxHealth = 4;
		this.width = 25;
		this.height = 50;
		this.barrel = (int) -(height * (.75));
		this.base = new Rectangle(x, y, 55, 55);
		this.rotateMult = 3;
		this.bulletSize = 5;

	}
	
	/*
	 * This method simply resets the tank's health and bounds.
	 */
	public void reset() {
		this.health = this.maxHealth;
		this.state = this.defaultState;
		this.bulletList = new ArrayList<Bullet>();
		this.base = new Rectangle(state[0], state[1], 60, 60);
	}

	@Override
	public String getType() {
		return "Defaulty";
	}

}

class QuickTank extends Tank implements Serializable {
	private static final long serialVersionUID = 1L;

	public QuickTank(int x,int y, String tankColor,int rotateState,String name) {
		super(x,y,tankColor,name);
		this.state = new int[] { x, y, rotateState };
		this.defaultState = new int[] { x, y, rotateState };
		this.xState = new int[] { 0, 9, 13, 9, 0, -9, -13, -9 };
		this.yState = new int[] { -13, -9, 0, 9, 13, 9, 0, -9 };
		this.health = 3;
		this.maxHealth = 3;
		this.width = 25;
		this.height = 30;
		this.barrel = (int) -(height * (.75));
		this.base = new Rectangle(x, y, 35, 35);
		this.rotateMult = 1;
		this.bulletSize = 3;
	}
	
	/*
	 * This method simply resets the tank's health and bounds.
	 */
	public void reset() {
		this.health = this.maxHealth;
		this.state = this.defaultState;
		this.bulletList = new ArrayList<Bullet>();
		this.base = new Rectangle(state[0], state[1], 60, 60);
	}

	@Override
	public String getType() {
		return "Quicky";
	}
}

class BigTank extends Tank implements Serializable {

	private static final long serialVersionUID = 1L;

	public BigTank(int x,int y, String tankColor,int rotateState,String name) {
		super(x,y,tankColor,name);
		this.state = new int[] { x, y, rotateState };
		this.defaultState = new int[] { x, y, rotateState };
		this.xState = new int[] { 0, 5, 7, 5, 0, -5, -7, -5 };
		this.yState = new int[] { -7, -5, 0, 5, 7, 5, 0, -5 };
		this.health = 6;
		this.maxHealth = 6;
		this.width = 40;
		this.height = 55;
		this.barrel = (int) -(height * (.75));
		this.base = new Rectangle(x, y, 60, 60);
		this.rotateMult = 6;
		this.bulletSize = 7;

	}
	
	/*
	 * This method simply resets the tank's health and bounds.
	 */
	public void reset() {
		this.health = this.maxHealth;
		this.state = this.defaultState;
		this.bulletList = new ArrayList<Bullet>();
		this.base = new Rectangle(state[0], state[1], 60, 60);
	}

	@Override
	public String getType() {
		return "Biggy";
	}

}