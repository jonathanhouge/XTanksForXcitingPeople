/*
* AUTHOR: Julius Ramirez [modified David code]
* FILE: Bullet.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to create an abstract class that defines what subclasses of Bullet can do.
* Subclasses of bullet include: Defaulty (DefaultBullet), Quicky (QuickBullet), and Biggy (BigBullet).
* 
*/
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class Bullet {
	protected int[] coordinates; // [x,y]
	protected int shiftX;
	protected int shiftY;
	protected Color color;

	public Bullet(Color color, int x, int y, int shiftX, int shiftY) {
		this.color = color;
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
		return rect.contains(coordinates[0], coordinates[1]);
	}
	
	public abstract void draw(GC gc);

}

class DefaultBullet extends Bullet {

	public DefaultBullet(Color color, int x, int y, int shiftX, int shiftY) {
		super(color, x, y, shiftX, shiftY);
	}

	@Override
	public void draw(GC gc) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], 5, 5);
	}
}

class QuickBullet extends Bullet {

	public QuickBullet(Color color, int x, int y, int shiftX, int shiftY) {
		super(color, x, y, shiftX, shiftY);
	}

	@Override
	public void draw(GC gc) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], 2, 2);
	}
}

class BigBullet extends Bullet {
	public BigBullet(Color color, int x, int y, int shiftX, int shiftY) {
		super(color, x, y, shiftX, shiftY);
	}

	@Override
	public void draw(GC gc) {
		gc.setBackground(color);
		coordinates[0] += shiftX;
		coordinates[1] += shiftY;
		gc.fillRectangle(coordinates[0], coordinates[1], 7, 7);
	}
}
