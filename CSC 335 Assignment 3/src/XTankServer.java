/* The server object. One of the players will need to run this code and, after, they
 * run client (XTank). This holds onto all of the settings and players.
 * 
 * AUTHOR: Jonathan [modified David Code]
 */

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executors;

/**
 * When a client connects, a new thread is started to handle it.
 */
public class XTankServer {
	static ArrayList<DataOutputStream> sq;
	static int currentPlayers = 0;
	static int playerCount = 1;
	static int ready = 0;
	static Settings settings;
	static Player[] players = new Player[4];
	public static void main(String[] args) throws Exception {
		System.out.println(InetAddress.getLocalHost());
		sq = new ArrayList<>();
		
		try (var listener = new ServerSocket(59896)) {
			System.out.println("The XTank server is running...");
			var pool = Executors.newFixedThreadPool(20);
			while (true) {
				pool.execute(new XTankManager(listener.accept(), currentPlayers));
			}
		}
	}
	public static void addPlayer(Player p) {
		for(int i = 0;i<4;i++) {
			if(players[i] == null) {
				players[i] = p;
				return;
			}
		}
	}
	private static class XTankManager implements Runnable {

		private Socket socket;
		private int playerNum;
		Player player;
		XTankManager(Socket socket, int player) {
			this.socket = socket;
			this.playerNum = player + 1;
			currentPlayers++;
			System.out.println("A brand new XTankManager has been created!");
		}
		
		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());

				sq.add(out);

				if (playerNum == 1) {
					out.writeInt(1); 					   		// Notifys XTank that this is the first player
					settings = (Settings) inObj.readObject();	// Recieves Settings from XTank
					playerCount = settings.players;				// Store players
				} else {
					out.writeInt(0);
				}

				this.player = (Player) inObj.readObject(); 		// player recieved!
				ready++;										// increase playersReady
				WaitingDialog wait = new WaitingDialog(); 		// create waiting dialog to let user know game not ready
				int leave;
				if (ready != playerCount) { 					// if playersReady != playerCount, send a waiting dialog to user
					outObj.writeObject(wait);
					leave = 0;
				} else {										// else, don't send the object out.
					leave = 1;
					outObj.writeObject(null); 
				}

				while (leave == 0) {							// this while loop keeps the player's waiting until all players have readied up
					if (ready == playerCount) {					// by constantly running until readyCount = playerCount
						leave = 1;
					}
				}
				
				addPlayer(player);
				System.out.println("About to send the player list!");
				outObj.writeObject(players);
				System.out.println("Player list has been sent by " + socket);
				
				
				out.writeInt(1);								// This writes out a 1 to the server so that it may exit the start loop and create a UI

				

				
				
				
				
				
				
				
				
				
				
				
				
				
				
				// Code below not important right now
//				int ycoord;
//				while (true) {
//					ycoord = in.readInt();
//					//System.out.println("ycoord = " + ycoord);
//					for (DataOutputStream o : sq) {
//						//System.out.println("o = " + o);
//						o.writeInt(ycoord);
//					}
//				}
			}

			catch (Exception e) {
				System.out.println("Error:" + socket);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println("Closed: " + socket);
			}
		}
	}
}