/* The map abstract class and implementations.
 * 
 * AUTHOR: Jonathan
 */

import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class Map {
	// boundaries (currently my local machine's hardcoded [1366 x 768])
	int x = 1366; int y = 768;
	int offset = 100; // if a player goes diagonally into a corner, we wanna catch it!
	ArrayList<Wall> borders = new ArrayList<Wall>();
	
	// constructor - eventually take in bounds
	public Map() {
		Wall top = new Wall(0 - offset, 0, x + (2 * offset), -y); Wall left = new Wall(0, 0, -x, y);
		Wall bottom = new Wall(x + offset, y, -x - (2* offset), y); Wall right = new Wall(x, y, x, -y);

		// test rectangles and make sure they're being drawn 
//		Wall top = new Wall(600, 400, x, y); Wall bottom = new Wall(1200, 0, 60, 70);
//		Wall left = new Wall(x/2, y/3, 70, 70); Wall right = new Wall(0, 0, 600, 50);
		borders.add(top); borders.add(left); 
		borders.add(bottom); borders.add(right);
	}

	public void draw(GC gc) {
		drawBorder(gc);
	}
	
	protected void drawBorder(GC gc) {
		for (Wall w : borders) { w.draw(gc); }
//		gc.drawLine(0, 0, x, 0);
//		gc.drawLine(0, 0, 0, y);
//		gc.drawLine(x, y, x, 0);
//		gc.drawLine(x, y, 0, y);
	}
	public ArrayList<Wall> getWalls(){
		return borders;			//TODO return ALL walls in the map
	}

}

// basically just a blank map!
class Plain extends Map { }

// draws the borders, then more
class Default extends Map {
	ArrayList<Wall> walls = new ArrayList<Wall>();
	
	public void draw(GC gc) {
		drawBorder(gc);
	}
}

// draws the borders, then more
class Maze extends Map {
	ArrayList<Wall> walls = new ArrayList<Wall>();
	
	public void draw(GC gc) {
		drawBorder(gc);
	}
	
}