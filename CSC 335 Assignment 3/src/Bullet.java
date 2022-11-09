/*
* AUTHOR: Julius Ramirez [modified David code]
* FILE: Bullet.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to create an abstract class that defines what subclasses of Bullet can do.
* Subclasses of bullet include: Defaulty (DefaultBullet), Quicky (QuickBullet), and Biggy (BigBullet).
* 
*/
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class Bullet {
	public abstract void draw(GC gc);
	public abstract boolean withinBounds(Rectangle bounds);
	public abstract int[] getCoordinates();
	public abstract boolean hasHit(Rectangle rect);
}
