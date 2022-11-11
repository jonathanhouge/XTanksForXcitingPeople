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
import java.util.ArrayList;

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
	
	private int playerID;
	private Player player;
	//private DefaultTank tank;
	private Map map;
	private static volatile Player[] playerArr;
	public XTankUI(DataInputStream in, DataOutputStream out, int height, int width, int playerID,Player[] playerArr) {
		this.in = in; this.out = out; 
		this.shellHeight = height; this.shellWidth = width;
		this.playerID = playerID--;
		this.player = playerArr[playerID];
		setPlayers(playerArr);
		System.out.println("XTANKUI constructor playerArr = " + playerArr);
	}
	private void setPlayers(Player[] players) {
		if(playerArr !=null) {
			return;
		}
		XTankUI.playerArr = players;
	}
	public void start() {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setText("xtank");
		shell.setLayout(new FillLayout());

		canvas = new Canvas(shell, SWT.NO_BACKGROUND);
		map = new Plain();
		map.borders.add(new Wall(300, 300, 50, 100));
		map.borders.add(new Wall(500,500, -50, -100));
		canvas.addPaintListener(event -> {
			//System.out.println("XTANKUI printing playerArr memoryAdd: " + playerArr);
			event.gc.fillRectangle(canvas.getBounds());
			map.draw(event.gc);
			for(Player playerInst: playerArr) {
				if(playerInst != null) {
					//System.out.println("XTANKUI PRINTING TANK DATA :" + playerInst.getTank());
					playerInst.getTank().draw(event.gc);
					playerInst.getTank().drawBullets(event.gc,map.getWalls(),playerArr);
				}
			}
			//System.out.println("XTANKUI canvasPaintListener finished drawing!");

			 });

		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.character == 'd' || e.keyCode == 16777220) {// RIGHT 
					player.getTank().turnRight();
				}else if (e.character == 'a' || e.keyCode == 16777219) {// LEFT
					player.getTank().turnLeft();
				}
				if(e.character == 's' || e.keyCode == 16777218) {// BACK
					player.getTank().moveBackward(map.getWalls(),playerArr);

				}else if(e.character == 'w' || e.keyCode == 16777217) {// FORWARD
					//System.out.println("XTANKUI.JAVA Forward button pressed,  calling method!");
					//System.out.println("XTANKUI.JAVA tank data BEFORE: " + player.getTank());
					player.getTank().moveForward(map.getWalls(),playerArr);
					//System.out.println("XTANKUI.JAVA tank data AFTER: " + player.getTank());
					//System.out.println("XTANKUI.JAVA Forward button pressed,  method done!");
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