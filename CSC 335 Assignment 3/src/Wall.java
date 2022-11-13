/* The wall class. Makes walls for Map, only used by Map.
 * Fills rectangles when asked to by map, always black due to color setting in UI.
 * 
 * AUTHOR: Jonathan Houge
 */

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class Wall {
	private Rectangle wall;
	
	// constructor - makes a wall of itself
	public Wall(int x, int y, int width, int height) {
		this.wall = new Rectangle(x, y, width, height); }
	
	// draw the wall
	public void draw(GC gc) {
		gc.fillRectangle(wall); }
	
	public Rectangle getWall() { return this.wall; }
	
	// String representation
	public String toString() { return wall.toString(); }
}