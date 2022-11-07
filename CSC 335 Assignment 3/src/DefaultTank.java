/*
* AUTHOR: Julius Ramirez
* FILE: DefaultTank.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to implement a default tank which is a subclass of the Tank class. 
* Movement in the x and y direction are based on the current rotate state of the tank. Rotating clockwise adds one 
* to the rotate state count while counter clockwise subtracts one. The number is then used to index into an array that
* updates the x and y values in the expected direction. 
*/

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;

public class DefaultTank extends Tank {
	private int[] state; // [x,y,rotateState]
	private int rotateState; // What rotate stage the tank is currently in
	private Color color;
	private Color armColor;
	private int width = 50;
	private int height = 100;
	private int barrel = (int) -(height * (.75));
	private int[] xState = { 0, 5, 10, 5, 0, -5, -10, -5 };
	private int[] yState = { -10, -5, 0, 5, 10, 5, 0, -5 };
	private int health = 1;
	
	/*
	 * This constructor sets the color of the tanks body, arm, and also starting position.
	 */
	public DefaultTank(Color color, Color color2) {
		this.state = new int[] { 300, 500, 0 };
		this.color = color;
		this.armColor = color2;

	}

	/*
	 * This method draws the tank onto the canvas. Because tanks can be rotated, it's necessary to 
	 * briefly translate the tank so that it rotates around itself rather than rotating around
	 * the origin of the canvas. 
	 */
	@Override
	public void draw(GC gc) {
		//gc = gc1;
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
	 * This method increases the rotate state by one then checks if the number is
	 * bounded correctly
	 */
	@Override
	public void turnRight() {
		rotateState++;
		checkRotateState();
	}

	/*
	 * This method decreases the rotate state by one then checks if the number is
	 * bounded correctly
	 */
	@Override
	public void turnLeft() {
		rotateState--;
		checkRotateState();
	}

	/*
	 * This method ensures that the rotate state is within bounds, setting the value
	 * to either zero or one less than the size of however many states the tank has.
	 */
	private void checkRotateState() {
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
	@Override
	public void moveForward() {

		state[1] += yState[rotateState];
		state[0] += xState[rotateState];

	}

	/*
	 * This method updates the X and Y values of the tank by subtracting the proper
	 * values based on the rotate state of the tank
	 */
	@Override
	public void moveBackward() {
		state[1] -= yState[rotateState];
		state[0] -= xState[rotateState];

	}

	@Override
	public void shoot() {
		// TODO Auto-generated method stub

	}
	

}
