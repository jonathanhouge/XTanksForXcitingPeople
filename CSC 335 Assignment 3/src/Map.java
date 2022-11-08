/* The map abstract class and implementations.
 * 
 * AUTHOR: Jonathan
 */

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class Map {
	int x = 1366;
	int y = 768;
	// get with Rectangle bounds = shell.getMonitor().getBounds();
	
	public void draw(GC gc) {
		gc.drawLine(0, 0, x, 0);
		gc.drawLine(0, 0, 0, y);
		gc.drawLine(x, y, x, 0);
		gc.drawLine(x, y, 0, y); }

}

class Plain extends Map {
	
}

class Default extends Map {
	
}

class Maze extends Map {
	
}