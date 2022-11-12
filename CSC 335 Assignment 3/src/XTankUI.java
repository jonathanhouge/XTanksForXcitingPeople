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
import java.util.Arrays;

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
	
	public XTankUI(DataInputStream in, DataOutputStream out, int height, int width, int playerID, Player[] playerArr) {
		this.in = in; this.out = out; 
		this.shellHeight = height; this.shellWidth = width;
		this.playerID = playerID--;
		this.player = playerArr[playerID];
		setPlayers(playerArr);
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
		map = new Default();
		canvas.addPaintListener(event -> {
			canvas.setBackground(event.gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
			event.gc.fillRectangle(canvas.getBounds());
			event.gc.setBackground(event.gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
			map.draw(event.gc);
			for(Player playerInst: playerArr) {
				if(playerInst != null) {
					playerInst.getTank().draw(event.gc);
					playerInst.getTank().drawBullets(event.gc, map.getWalls(), playerArr);
				}
			}
			for(Player playerInst: playerArr) { // This loop happens again because bullets may kill player
				if(playerInst != null) {
					playerInst.getTank().draw(event.gc);
				}
			}
			checkGameStatus();
			 });

		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(player.getTank().isAlive()) {
					if(e.character == 'd' || e.keyCode == 16777220) {// RIGHT 
						player.getTank().turnRight();
					}else if (e.character == 'a' || e.keyCode == 16777219) {// LEFT
						player.getTank().turnLeft();
					}
					if(e.character == 's' || e.keyCode == 16777218) {// BACK
						player.getTank().moveBackward(map.getWalls(),playerArr);

					}else if(e.character == 'w' || e.keyCode == 16777217) {// FORWARD
						player.getTank().moveForward(map.getWalls(),playerArr);
					}else if (e.character == ' ' || e.keyCode == 32) {
						player.getTank().shoot();
					}
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
	
	
	/*
	 * This method checks if the game is over or not, TODO for now it prints out statement but 
	 * later add server call
	 */
	private void checkGameStatus() {
		int alive = 0;
		for(Player player:playerArr) {
			if(player !=null) {
				if (player.getTank().isAlive()) {
					alive++;
				}
			}
		}
		if (alive <= 1) {
			System.out.println("The game is over! One or less players are alive!");
		}
	}



	class Runner implements Runnable {
		public void run() {

            display.timerExec(150, this); } }	
}