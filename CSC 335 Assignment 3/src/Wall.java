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
		gc.drawRectangle(wall);
	}

}