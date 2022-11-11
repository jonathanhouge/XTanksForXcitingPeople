/*
* AUTHOR: Julius Ramirez [modified David code]
* FILE: Bullet.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to create an abstract class that defines what subclasses of Bullet can do.
* Subclasses of bullet include: Defaulty (DefaultBullet), Quicky (QuickBullet), and Biggy (BigBullet).
* 
*/
import java.io.Serializable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class Bullet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int[] coordinates; // [x,y]
	protected int shiftX;
	protected int shiftY;
	
	public Bullet(int x, int y, int shiftX, int shiftY) {		
		this.coordinates = new int[] { x, y };
		this.shiftX = shiftX;
		this.shiftY = shiftY;
	}
	
	public boolean withinBounds(Rectangle bounds) {
		if (coordinates[0] < 0 || coordinates[1] < 0 || coordinates[0] > bounds.width
				|| coordinates[1] > bounds.height) {
			return false;
		}
		return true;
	}

	public int[] getCoordinates() {
		return coordinates;

	}

	public boolean hasHit(Rectangle rect) {
		System.out.println("DEBUG rectData, (bullet) x,y:" + rect.toString() + coordinates[0] +',' + coordinates[1]);
		return rect.contains(coordinates[0], coordinates[1]);
	}
	
	public abstract void draw(GC gc, Color color);

}

class DefaultBullet extends Bullet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DefaultBullet(int x, int y, int shiftX, int shiftY) {
		super(x, y, shiftX, shiftY);
	}

	@Override
	public void draw(GC gc,Color color) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], 5, 5);
	}
}

class QuickBullet extends Bullet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuickBullet(Color color, int x, int y, int shiftX, int shiftY) {
		super(x, y, shiftX, shiftY);
	}

	@Override
	public void draw(GC gc,Color color) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], 3, 3);
	}
}

class BigBullet extends Bullet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BigBullet(int x, int y, int shiftX, int shiftY) {
		super(x, y, shiftX, shiftY);
	}

	@Override
	public void draw(GC gc,Color color) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], 7, 7);
	}
}
