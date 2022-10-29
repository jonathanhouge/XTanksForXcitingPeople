import org.eclipse.swt.graphics.GC;

public abstract class Tank {
	private int[] state; //x,y,rotation
	
	//public abstract void draw();
	public abstract void turnRight();
	public abstract void turnLeft();
	public abstract void moveForward();
	public abstract void moveBackward();
	public abstract void shoot();
	public void draw(GC gc1) {
		// TODO Auto-generated method stub
		
	}
}
