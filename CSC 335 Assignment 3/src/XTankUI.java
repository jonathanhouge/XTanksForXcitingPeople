/*
* AUTHOR: Julius Ramirez [modified David code]
* FILE: XTankUI.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: 
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
	
	// our bounds
	private int shellHeight;
	private int shellWidth;
	
	private Canvas canvas;
	private Display display;
	DataInputStream in; 
	DataOutputStream out;
	
	private Player player;
	//private DefaultTank tank;
	private Map map;
	
	public XTankUI(DataInputStream in, DataOutputStream out, int height, int width, Player player) {
		this.in = in; this.out = out; 
		this.shellHeight = height; this.shellWidth = width;
		this.player = player;
		// later should take in chosen map object
	}
	
	public void start() {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setText("xtank");
		shell.setLayout(new FillLayout());

		canvas = new Canvas(shell, SWT.NO_BACKGROUND);
		map = new Plain();
		//this.tank = new DefaultTank(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN), shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		canvas.addPaintListener(event -> {
			event.gc.fillRectangle(canvas.getBounds());
			map.draw(event.gc);
			event.gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			event.gc.fillRectangle(300, 300, 50, 100);
			//tank.updateGC(event.gc);
			player.getTank().draw(event.gc);
			player.getTank().drawBullets(event.gc);
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
				if(e.character == 'd' || e.keyCode == 16777220) {// RIGHT MOVEMENT
					player.getTank().turnRight();
				}else if (e.character == 'a' || e.keyCode == 16777219) {// LEFT MOVEMENT
					player.getTank().turnLeft();
				}
				if(e.character == 's' || e.keyCode == 16777218) {
					player.getTank().moveBackward();

				}else if(e.character == 'w' || e.keyCode == 16777217) {
					player.getTank().moveForward();
				}else if (e.character == ' ' || e.keyCode == 32) {
					player.getTank().shoot();
				}

				canvas.redraw(); }

			public void keyReleased(KeyEvent e) {} });
	
		Runnable runnable = new Runner();
		display.asyncExec(runnable);
		shell.open();
		while (!shell.isDisposed()) 
			if (!display.readAndDispatch())
				display.sleep();

		display.dispose(); }
	
	
	
	class Runner implements Runnable {
		public void run() {

            display.timerExec(150, this); } }	
}