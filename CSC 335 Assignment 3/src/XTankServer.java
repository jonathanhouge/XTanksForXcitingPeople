/* The server object. One of the players will need to run this code and, after, they
 * run client (XTank). This holds onto all of the settings and players.
 * 
 * AUTHOR: Jonathan & Julius [modified David Code]
 */

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class XTankServer {
	static ArrayList<DataOutputStream> sq;
	static int currentPlayers = 0;
	static int playerCount = 1;
	static volatile int ready = 0;
	static Settings settings;
	static volatile Player[] players = new Player[4];
	private static Lock lock = new ReentrantLock(true); // true = fair lock

	public static void main(String[] args) throws Exception {
		System.out.println(InetAddress.getLocalHost());
		sq = new ArrayList<>();

		try (var listener = new ServerSocket(59896)) {
			System.out.println("The XTank server is running...");
			var pool = Executors.newFixedThreadPool(4); // only want four players
			while (true) {
				// new player has joined, make a new manager
				pool.execute(new XTankManager(listener.accept(), currentPlayers));
			}
		}
	}
	
	/*
	 * This method is responsible for building the array of Player objects.
	 */
	public static void addPlayer(Player p) {
		for (int i = 0; i < 4; i++) {
			if (players[i] == null) {
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
			System.out.println("A brand new XTankManager has been created!"); }

		@Override
		public void run() {

			System.out.println("Connected: " + socket);
			try {
				lock.lock(); // lock created so only one manager gets data. No corruption possible
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());

				sq.add(out);
				out.writeInt(playerNum); 						// Sends XTank playerID
				if (playerNum == 1) {
					settings = (Settings) inObj.readObject(); 	// Receives Settings from XTank
					playerCount = settings.getPlayers(); 			// Store players
				}
				
				outObj.writeObject(settings); // send settings to every client

				this.player = (Player) inObj.readObject(); 		// player created!
				addPlayer(player); 								// add player to array of players
				ready++; 										// increase playersReady

				lock.unlock(); // unlocks thread so other players can now make their own tank
				
				WaitingDialog wait = new WaitingDialog(); 		// create waiting dialog to let user know game not ready
				int leave; 										
				
				// if playersReady != playerCount, send a waiting dialog to user
				if (ready != playerCount) { 
					outObj.writeObject(wait); 					// create waiting dialog created
					leave = 0; 
				} else {
					leave = 1;
					outObj.writeObject(null);
				}
				
				// this while loop keeps the server stagnant until all players have created their tank
				while (leave == 0) { 
					if (ready == playerCount) {
						leave = 1;
					}
				}

				// This lock allows ALL managers to be notified game ready and playerArray to be sent properly
				lock.lock(); 	
				out.writeInt(1); 
				outObj.writeObject(players);
				lock.unlock(); 
				// Code below handles game loop
                while (true) {
                    int command = in.readInt();	// command has been received, must notify UI
                    System.out.println("XTANKSERVER recieved command " + command);
                    lock.lock();
                    for (DataOutputStream o: sq)	// For each player in server
                    {
                        o.writeInt(command);		// Send them command so they can update accordingly
                    }
                    lock.unlock();
                }
			}

			catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Error:" + socket);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("XTANKSERVER COULD NOT CLOSE SOCKET EXCEPTION");
				}
				System.out.println("Closed: " + socket);
			}
		}
	}
}