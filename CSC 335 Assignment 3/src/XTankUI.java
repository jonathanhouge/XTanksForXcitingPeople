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
	private Settings settings;
	
	public XTankUI(DataInputStream in, DataOutputStream out, int width, int height, int playerID, Player[] playerArr, Settings settings) {
		this.in = in; this.out = out; 
		this.shellHeight = height; this.shellWidth = width;
		this.playerID = playerID--;
		this.player = playerArr[playerID];
		setPlayers(playerArr);
		this.settings = settings;
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
		shell.setText("XTank: Player " + this.playerID);
		shell.setLayout(new FillLayout());
		
		canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		map = settings.giveMap(this.shellWidth, this.shellHeight);
		
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
			
			// standard will end the game when only one player reminas
			if(settings.getRules().equals("Standard")) {
				checkGameStatus(); }
			 });

		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(player.getTank().isAlive()) {
					if(e.character == 'd' || e.keyCode == 16777220) {// RIGHT 
						//player.getTank().turnRight();
						try {
							out.writeInt(playerID*10 + 2);
						} catch (IOException e1) {
							System.out.println("XTANK UI ERROR IN KEY LISTENER, CANT SEND INT");
						}
					}else if (e.character == 'a' || e.keyCode == 16777219) {// LEFT
						//player.getTank().turnLeft();
						try {
							out.writeInt(playerID*10 + 3);
						} catch (IOException e1) {
							System.out.println("XTANK UI ERROR IN KEY LISTENER, CANT SEND INT");
						}
					}
					if(e.character == 's' || e.keyCode == 16777218) {// BACK
						//player.getTank().moveBackward(map.getWalls(),playerArr);
						try {
							out.writeInt(playerID*10 + 1);
						} catch (IOException e1) {
							System.out.println("XTANK UI ERROR IN KEY LISTENER, CANT SEND INT");
						}

					}else if(e.character == 'w' || e.keyCode == 16777217) {// FORWARD
						//player.getTank().moveForward(map.getWalls(),playerArr);
						try {
							out.writeInt(playerID*10);
						} catch (IOException e1) {
							System.out.println("XTANK UI ERROR IN KEY LISTENER, CANT SEND INT");
						}
					}else if (e.character == ' ' || e.keyCode == 32) {
						//player.getTank().shoot();
						try {
							out.writeInt(playerID*10 + 4);
						} catch (IOException e1) {
							System.out.println("XTANK UI ERROR IN KEY LISTENER, CANT SEND INT");
						}
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

		System.out.println("XTANKUI ABOUT TO DISPOSE DISPLAY");
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
			// close dialog & make a new dialog that informs player
		}
	}

	class Runner implements Runnable {
		public void run() {
			int command;
			try {
				if(in.available() > 0) {
					System.out.println("REACH");
					command = in.readInt();
					int identify = ((int)(command % 100) / 10)-1;
		            int action = command%10;
		            System.out.println("Player #: " + identify);
		            System.out.println("Action #: " + action);
		            Player player = playerArr[identify];
		            Tank tank = player.getTank();
		            if(action == 0) {    // Move forward
		                tank.moveForward(map.getWalls(),playerArr);
		            }else if (action == 1) {// Move backwards
		                tank.moveBackward(map.getWalls(),playerArr);
		            } else if (action == 2) { // Rotate right
		                tank.turnRight();
		            }else if (action == 3) { // Rotate left
		                tank.turnLeft();
		            }else if (action == 4) { // Reduce health
		                tank.shoot();
		            }
				}
			} catch (Exception e) {
				System.out.println("XTANKUI RUNNER DIND'T GET INT?");
			}
			canvas.redraw();
            display.timerExec(30, this); 
            } }	
}