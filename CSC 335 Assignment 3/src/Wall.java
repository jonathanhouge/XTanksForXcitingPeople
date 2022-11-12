/* The wall class.
 * 
 * AUTHOR: Jonathan
 */

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class Wall {
	private Rectangle wall;
	
	public Wall(int x, int y, int width, int height) {
		this.wall = new Rectangle(x, y, width, height); }
	
	public void draw(GC gc) {
		gc.fillRectangle(wall); // was draw, now fill
	}
	
	public Rectangle getWall() {
		return this.wall;
	}
	/*
	 * This method is for debugging, printing out the rectangle data
	 * of this wall.
	 */
	public String toString() {
		return wall.toString();
	}
}