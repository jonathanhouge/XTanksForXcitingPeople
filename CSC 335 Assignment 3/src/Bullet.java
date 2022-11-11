
/*
* AUTHOR: Julius Ramirez
* FILE: Bullet.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to create an abstract class that implements methods for the subclasses of Bullet.
* A bullet is a projectile that is shot out of the tank and eventually hits a wall or player.
* 
* Subclasses of bullet include: Defaulty (DefaultBullet), Quicky (QuickBullet), and Biggy (BigBullet).
* 
*/
import java.io.Serializable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class Bullet implements Serializable {

	private static final long serialVersionUID = 1L;
	protected int[] coordinates; // [x,y]
	protected int shiftX;
	protected int shiftY;
	protected int size;

	/*
	 * This constructor sets the current x/y position of the bullet and a shiftX/shiftY which
	 * is used to determine where the bullet will be next time it is updated.
	 */
	public Bullet(int x, int y, int shiftX, int shiftY) {
		this.coordinates = new int[] { x, y };
		this.shiftX = shiftX;
		this.shiftY = shiftY;
	}

	public boolean hasHit(Rectangle rect) {
		return rect.contains(coordinates[0], coordinates[1]);
	}

	public void draw(GC gc, Color color) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], size, size);
	}

}

class DefaultBullet extends Bullet {

	private static final long serialVersionUID = 1L;

	public DefaultBullet(int x, int y, int shiftX, int shiftY) {
		super(x, y, shiftX, shiftY);
		this.size = 5;
	}

}

class QuickBullet extends Bullet {
	private static final long serialVersionUID = 1L;

	public QuickBullet(Color color, int x, int y, int shiftX, int shiftY) {
		super(x, y, shiftX, shiftY);
		this.size = 3;

	}

}

class BigBullet extends Bullet {

	private static final long serialVersionUID = 1L;

	public BigBullet(int x, int y, int shiftX, int shiftY) {
		super(x, y, shiftX, shiftY);
		this.size = 7;

	}

}
