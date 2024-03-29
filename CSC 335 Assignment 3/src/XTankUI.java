/*
* AUTHOR: Julius Ramirez [modified David code]
* FILE: XTankUI.java
* ASSIGNMENT: Assignment 2 - Xtanks
* COURSE: CSc 335; Fall 2022
* PURPOSE: 
*/

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XTankUI {
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
	private Player[] playerArr;
	private Settings settings;
	private boolean playing;
	private Runner runnable;
	
	// constructor
	public XTankUI(DataInputStream in, DataOutputStream out, int width, int height, int playerID, Player[] playerArr, Settings settings) {
		this.in = in; this.out = out;
		this.shellHeight = height; this.shellWidth = width;
		this.playerID = playerID - 1;
		this.player = playerArr[this.playerID];
		this.playerArr = playerArr;
		this.settings = settings;
	}
	
	public void start() throws IOException {
		display = new Display();
		
		System.out.println("XTANKUI CANVAS START");
		Shell shell = new Shell(display);
		shell.setText("XTank: Player " + (this.playerID + 1));

		shell.setLayout(new FillLayout());
		
		canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		map = settings.giveMap(this.shellWidth, this.shellHeight);
		this.playing = true;
		
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
			
			// standard will end the game when only one player remains
			if(settings.getRules().equals("Standard")) {
				checkGameStatus(); }
			 });

		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(player.getTank().isAlive()) {
					System.out.println("XTANKUI KEYLISTENER playerID = " + playerID);

					if(e.character == 'd' || e.keyCode == 16777220) {// RIGHT 
						try {
							out.writeInt(playerID*10 + 2);
						} catch (IOException e1) {
							System.out.println("XTANKUI: Unable to send int in KL!");
						}
					}else if (e.character == 'a' || e.keyCode == 16777219) {// LEFT
						try {
							out.writeInt(playerID*10 + 3);
						} catch (IOException e1) {
							System.out.println("XTANKUI: Unable to send int in KL!");
						}
					}
					if(e.character == 's' || e.keyCode == 16777218) {// BACK
						try {
							out.writeInt(playerID*10 + 1);
						} catch (IOException e1) {
							System.out.println("XTANKUI: Unable to send int in KL!");
						}

					}else if(e.character == 'w' || e.keyCode == 16777217) {// FORWARD
						try {
							out.writeInt(playerID*10);
						} catch (IOException e1) {
							System.out.println("XTANKUI: Unable to send int in KL!");
						}
					}else if (e.character == ' ' || e.keyCode == 32) { // SHOOT
						try {
							out.writeInt(playerID*10 + 4);
						} catch (IOException e1) {
							System.out.println("XTANKUI: Unable to send int in KL!");
						}
					}
				}
				canvas.redraw(); }
			public void keyReleased(KeyEvent e) {} });
		
		if (runnable == null) {
			runnable = new Runner(); }
		display.asyncExec(runnable);
		
		shell.open();
		
		while (this.playing) { // while all players are alive (in Standard mode), keep event loop up
			if (!display.readAndDispatch()) {
				display.sleep(); } }

		boolean result = playerArr[playerID].getTank().isAlive(); // did they win or lose?
		boolean playAgain = false; // default: won't play another time
		
		try {
			System.out.println("XTANKUI ABOUT TO DISPOSE DISPLAY");
			display.dispose();
			playAgain = (new XTankGameOverDisplay()).start(result); }
		
		// sometimes display dispose causes an error - but we're still able to function properly
		catch (Exception e) { 
			playAgain = (new XTankGameOverDisplay()).start(result); }
		
		if (playAgain) {
			out.writeInt(5); // tell server we're playing again to reset tank health, location, etc.
			start();
		}
	}
	
	
	/*
	 * This method checks if the game is over or not
	 */
	private void checkGameStatus() {
		int alive = 0;
		for(Player player:playerArr) {
			if(player !=null) {
				System.out.println("My health is: " + player.getTank().health);
				if (player.getTank().isAlive()) {
					alive++;
				}
			}
		}
		
		if (alive <= 1) {
			System.out.println("Game over!");
			this.playing = false;
		}
	}

	// server - client communication
	class Runner implements Runnable {
		public void run() {
			int command;
			try {
				if(in.available() > 0) {
					command = in.readInt();
					int identify = ((int)(command % 100) / 10);
		            int action = command%10;
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
		            }else if (action == 5) { // restart
		            	for(Player p : playerArr) {
		        			if(p !=null) {
		        				p.getTank().reset();
		        			}
		        		}
		            }
				}
			} catch (Exception e) {
				System.out.println("XTANKUI: Error in Runner");
			}
			canvas.redraw();
            display.timerExec(30, this); 
            } }	
}