/*
* AUTHOR: Julius Ramirez [modified David code]
* FILE: DefaultBullet.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: The purpose of this class is to implement a basic bullet that will move 
* across the screen in a linear motion whenever the player shoots. 
*/
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class DefaultBullet extends Bullet{
    private int[] coordinates; //[x,y]
    private int shiftX;
    private int shiftY;
    private Color color;
    public DefaultBullet(Color color, int x, int y,int shiftX, int shiftY) {
    	this.color = color;
        this.coordinates = new int[]{x,y};
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }
    @Override
    public void draw(GC gc){
        gc.setBackground(color);
        coordinates[0] += shiftX;
        coordinates[1] += shiftY;
        gc.fillRectangle(coordinates[0], coordinates[1], 5, 5);
    }
    @Override
    public boolean withinBounds(Rectangle bounds){
        if(coordinates[0] < 0 || coordinates[1] < 0 || coordinates[0] > bounds.width || coordinates[1] > bounds.height){
            return false;
        }
        return true;
    }
    @Override
    public int[] getCoordinates(){
        return coordinates;
    }
}