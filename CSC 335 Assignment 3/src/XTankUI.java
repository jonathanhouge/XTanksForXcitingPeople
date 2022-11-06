/*
 * 
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XTankUI {
	// The location and direction of the "tank"
	private int x = 300;
	private int y = 500;
	
	private Canvas canvas;
	private Display display;
	DataInputStream in; 
	DataOutputStream out;
	private DefaultTank tank;
	
	public XTankUI(DataInputStream in, DataOutputStream out) {
		this.in = in;
		this.out = out; 
	}
	
	public void start() {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setText("xtank");
		shell.setLayout(new FillLayout());

		canvas = new Canvas(shell, SWT.NO_BACKGROUND);
		this.tank = new DefaultTank(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN),shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		canvas.addPaintListener(event -> {
			event.gc.fillRectangle(canvas.getBounds());
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			event.gc.fillRectangle(300, 300, 50, 100);
			//tank.updateGC(event.gc);
			tank.draw(event.gc);
			System.out.println("PRINTING RECT");
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			event.gc.fillRectangle(500, 500, 50, 100);
			 });

		canvas.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				System.out.println("mouseDown in canvas");canvas.redraw(); } 

			public void mouseUp(MouseEvent e) {canvas.redraw();}
			
			public void mouseDoubleClick(MouseEvent e) {canvas.redraw();}
			});

		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.character == 'd' || e.character == 'D') {// RIGHT MOVEMENT
					tank.turnRight();
				}else if (e.character == 'a' || e.character == 'A') {// LEFT MOVEMENT
					tank.turnLeft();
				}
				if(e.character == 's' || e.character == 'S') {
					tank.moveBackward();

				}else if(e.character == 'w' || e.character == 'W') {
					tank.moveForward();
				}

				try {
					out.writeInt(y); }
				catch(IOException ex) {
					System.out.println("The server did not respond (write KL)."); }

				canvas.redraw(); }

			public void keyReleased(KeyEvent e) {} });

		try {
			out.writeInt(y); }
		catch(IOException ex) {
			System.out.println("The server did not respond (initial write)."); }		
		Runnable runnable = new Runner();
		display.asyncExec(runnable);
		shell.open();
		while (!shell.isDisposed()) 
			if (!display.readAndDispatch())
				display.sleep();

		display.dispose(); }
	
	class Runner implements Runnable {
		public void run() {
			try {
				if (in.available() > 0) {
					y = in.readInt();
					//System.out.println("y = " + y);
					canvas.redraw(); } }

			catch(IOException ex) {
				System.out.println("The server did not respond (async)."); }

            display.timerExec(150, this); } }	
}