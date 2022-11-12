/* The map abstract class and implementations.
 * 
 * AUTHOR: Jonathan
 */

import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;

public abstract class Map {
	// boundaries (currently my local machine's hardcoded [1366 x 768])
	int offset = 65; // to try and account for taskbar hiding part of map
	ArrayList<Wall> borders = new ArrayList<Wall>();
	
	// constructor - eventually take in bounds
	public Map(int x, int y) {
		Wall top = new Wall(1, 1, x, 1); Wall left = new Wall(1, 1, 1, y);
		Wall bottom = new Wall(1, y - offset, x, 1); Wall right = new Wall(x - 1, 1, 1, y);

		borders.add(top); borders.add(left); 
		borders.add(bottom); borders.add(right); }

	public void draw(GC gc) {
		drawBorder(gc); }
	
	protected void drawBorder(GC gc) {
		for (Wall w : borders) { w.draw(gc); } }
	
	public ArrayList<Wall> getWalls(){
		return borders; }

}

// basically just a blank map!
class Whitespace extends Map {

	public Whitespace(int x, int y) {
		super(x, y); } 
}

// default map - barriers in-between quadrants, little meet in the middle
class Courtyard extends Map {

	ArrayList<Wall> walls = new ArrayList<Wall>();
	int width = 50; int height = 10; // are swapped around in actual wall creation to create corresponding walls
	
	public Courtyard(int x, int y) {
		super(x, y);
		System.out.println("do you");
		
		// middle area
		walls.add(new Wall(x/2 - offset, y/2 - offset, width, height));
		walls.add(new Wall(x/2 - offset, y/2 - offset, height, width));
		walls.add(new Wall(x/2 + offset, y/2 + offset, width, height));
		walls.add(new Wall(x/2 + offset + 40, y/2 + offset - width, height, width));
		
		// horizontal
		walls.add(new Wall(1, y/2, width, height/2)); 
		walls.add(new Wall(1 + (4 * offset), y/2, width, height/2)); 
		walls.add(new Wall(1 + (6 * offset), y/2, width, height/2)); 
		walls.add(new Wall(1 - (6 * offset), y/2, width, height/2)); 
		walls.add(new Wall(x - (4 * offset), y/2, width, height/2)); 
		walls.add(new Wall(x - offset, y/2, width, height/2));
		
		// vertical
		walls.add(new Wall(x/2, y/height - offset, height/2, width)); 
		walls.add(new Wall(x/2, y/height + offset, height/2, width)); 
		walls.add(new Wall(x/2, y - (4 * offset), height/2, width));
		walls.add(new Wall(x/2, y - (2 * offset), height/2, width)); }
	
	public void draw(GC gc) {
		drawBorder(gc);
		drawObstacles(gc); }
	
	protected void drawObstacles(GC gc) {
		for (Wall w : walls) { w.draw(gc); } }
	
	public ArrayList<Wall> getWalls() {
		ArrayList<Wall> bordersCopy = new ArrayList<Wall>(borders);
		ArrayList<Wall> allWalls = new ArrayList<Wall>(walls);
		allWalls.addAll(bordersCopy);
		return allWalls; }
}

// draws the borders, then more
class Fortress extends Map {
	ArrayList<Wall> walls = new ArrayList<Wall>();
	int width = 100; int height = 10; // general purpose, but reorganized to create consistency
	
	// constructor - creates the maze
	public Fortress(int x, int y) {
		
		super(x, y);
		
		// horizontal walls, top left [player 1 spawn area]
		walls.add(new Wall(1, y/6, width, height));
		walls.add(new Wall(1 + width, y/6, width, height));
		walls.add(new Wall(1 + (3*width), y/6, width, height));
		
		// downward walls in the top-middle-left
		walls.add(new Wall(1 + (4*width), 1 + (2*width), height, width));
		walls.add(new Wall(1 + (4*width), 1, height, (2*width)));
		
		// horizontal wall, middle pointing right (off of above walls)
		walls.add(new Wall(1 + (4*width), 1 + (3*width), (2*width), height));
		
		// vertical wall, connected to above
		walls.add(new Wall(1 + (6*width), y/6, height, (2*width) - (height + (int) Math.ceil(height * 3/4)))); // arbitrary seven to line up

		// vertical wall, left side
		walls.add(new Wall(1 + (2*width), 1 + (3*width) - (3*height), height, (2*width)));
		
		// horizontal walls, bottom-left [player 3 spawn area]
		walls.add(new Wall(1, y - (2*width), (2*width), height));
		
		// vertical wall, bottom-middle-left-leaning (by above)
		walls.add(new Wall(1 + (4*width), y - (2*width), height, (2*width)));
		
		// horizontal wall, right of above
		walls.add(new Wall(1 + (4*width), y - (2*width), (2*width), height));
		
		// horizontal wall, top-left [player 2 spawn area]
		walls.add(new Wall(x - offset, y/6, (2*width), height));
		walls.add(new Wall(x - offset - (4*width), y/6, (4*width), height));
		
		// vertical wall, top-left (off of player 2 spawn walls)
		walls.add(new Wall(x - offset - (4*width), y/6, height, (2*width)));
		
		// horizontal wall, bottom-right [player 4 spawn area]
		walls.add(new Wall(x - offset, y - (3*offset), (2*width), height));
		walls.add(new Wall(x - offset - (4*width), y - (3*offset), (4*width), height));
	}
	
	public void draw(GC gc) {
		drawBorder(gc);
		drawObstacles(gc); }
	
	protected void drawObstacles(GC gc) {
		for (Wall w : walls) { w.draw(gc); } }
	
	public ArrayList<Wall> getWalls() {
		ArrayList<Wall> bordersCopy = new ArrayList<Wall>(borders);
		ArrayList<Wall> allWalls = new ArrayList<Wall>(walls);
		allWalls.addAll(bordersCopy);
		return allWalls; }
	
}