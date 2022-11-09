import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;

public class BigTank extends Tank implements Serializable {
	private static final long serialVersionUID = 1L;
	private int[] state; // [x,y,rotateState]
	private int rotateState; // What rotate stage the tank is currently in
	private transient Color color;
	private transient Color armColor;
	private int width = 40;
	private int height = 55;
	private int barrel = (int) -(height * (.75));
	private int[] xState = { 0, 5, 7, 5, 0, -5, -7, -5 };
	private int[] yState = { -7, -5, 0, 5, 7, 5, 0, -5 };
	private int health = 1;
	private transient List<Bullet> bulletList;
	/*
	 * This constructor sets the color of the tanks body, arm, and also starting position.
	 */
	public BigTank(Color color, Color color2) {
		this.state = new int[] { 300, 500, 0 };
		this.color = color;
		this.armColor = color2;
		this.bulletList = new ArrayList<>();
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

	/*
	 * This method 'shoots' a bullet by adding a bullet to the tanks 
	 * bulletList so that the bullet may be drawn and possibly interact 
	 * with other enemy tanks in the future.
	 */
	@Override
	public void shoot() {
		int rotateMult = 6;
		int xOffset = this.xState[rotateState]*rotateMult;
		int yOffset = this.yState[rotateState]*rotateMult;
		bulletList.add(new BigBullet(color, this.state[0]+xOffset,this.state[1]+yOffset,
				this.xState[rotateState],this.yState[rotateState]));
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
	
	public boolean hasHit(Rectangle rect) {
        for(Bullet bullet:bulletList) {
            if (bullet.hasHit(rect)) {
                return true;
            }
        }
        return false;
    }
}
