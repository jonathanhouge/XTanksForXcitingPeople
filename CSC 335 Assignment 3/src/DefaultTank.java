import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Transform;


public class DefaultTank extends Tank {
	private int[] state; //[x,y,rotateState]
	private int rotateState; //rotation
	private GC gc;
	private Color color;
	private Color armColor;
	private int rotateAmount = 0;
	private int[] xState = {0,5,10,5,0,-5,-10,-5};
	private int[] yState ={-10,-5,0,5,10,5,0,-5};
	
	public DefaultTank(GC gc,Color color,Color color2) {
		this.state = new int[] {300,500,0};
		this.gc = gc;
		this.color = color;
		this.armColor = color2;
		Transform oldTransform = new Transform(gc.getDevice());  
		oldTransform.translate(300, 500);
		gc.setTransform(oldTransform);
	}
	@Override
	public void draw() {
		gc.setBackground(color);
		gc.fillRectangle(state[0], state[1], 50, 100);
		gc.setBackground(armColor);
		gc.fillOval(state[0], state[1]+25, 50, 50);
		gc.setLineWidth(4);
		gc.drawLine(state[0]+25, state[1]+25, state[0]+25, state[1]-15);
	}
	@Override
	public void turnRight() {
		Transform oldTransform = new Transform(gc.getDevice());  
        gc.getTransform(oldTransform);
		oldTransform.rotate(45);
		gc.setTransform(oldTransform);
		rotateState++;
		checkRotateState();
	}

	private void checkRotateState() {
		if(rotateState <= -1) {
			rotateState = yState.length-1;
		}else if(rotateState >= yState.length) {
			rotateState = 0;
		}
	}
	@Override
	public void turnLeft() {
		Transform oldTransform = new Transform(gc.getDevice());  
        gc.getTransform(oldTransform);
		oldTransform.rotate(-45);
		gc.setTransform(oldTransform);
		rotateState--;
		checkRotateState();
	}

	@Override
	public void moveForward() {
		
		state[1] += yState[rotateState];
		state[0] += xState[rotateState];
		
	}

	@Override
	public void moveBackward() {
		state[1] -= yState[rotateState];
		state[0] -= xState[rotateState];
		
	}

	@Override
	public void shoot() {
		// TODO Auto-generated method stub
		
	}

}
