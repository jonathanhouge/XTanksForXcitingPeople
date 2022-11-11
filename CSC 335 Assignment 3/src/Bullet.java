
/*
* AUTHOR: Julius Ramirez
* FILE: Bullet.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to create a class that represents a tank's bullet.
* A bullet is a projectile that is shot out of the tank and eventually hits a wall or player.
* 
*/
import java.io.Serializable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class Bullet implements Serializable {

	private static final long serialVersionUID = 1L;
	protected int[] coordinates; // [x,y]
	protected int shiftX;
	protected int shiftY;
	protected int size;

	/*
	 * This constructor sets the current x/y position of the bullet and a
	 * shiftX/shiftY which is used to determine where the bullet will be next time
	 * it is updated.
	 */
	public Bullet(int x, int y, int shiftX, int shiftY,int size) {
		this.coordinates = new int[] { x, y };
		this.shiftX = shiftX;
		this.shiftY = shiftY;
	}

	/*
	 * This method is responsible for alerting the Tank class if a bullet has hit
	 * another tank, returning true if the bullet is within a tanks base, false if
	 * not.
	 */
	public boolean hasHit(Rectangle rect) {
		return rect.contains(coordinates[0], coordinates[1]);
	}

	/*
	 * This method is responsible for drawing a bullet on the user's screen. It
	 * first updates the bullet's location and then draws it onto the canvas.
	 */
	public void draw(GC gc, Color color) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], size, size);
	}

}
