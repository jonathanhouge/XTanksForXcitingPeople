/*
* AUTHOR: Julius Ramirez
* FILE: Tank.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this file is to create an abstract class that defines what subclasses of tank can do.
* Subclasses of tank include: Defaulty (DefaultTank), Quicky (QuickTank), and Biggy (BigTank).
*
*/

import org.eclipse.swt.graphics.GC;

public abstract class Tank {
	public abstract void turnRight();
	public abstract void turnLeft();
	public abstract void moveForward();
	public abstract void moveBackward();
	public abstract void shoot();
	public abstract void draw(GC gc1);
	protected abstract void drawBullets(GC gc);
	public abstract String getType();
}
