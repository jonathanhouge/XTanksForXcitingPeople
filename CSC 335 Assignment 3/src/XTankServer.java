/* The server object. One of the players will need to run this code and, after, they
 * run client (XTank). This holds onto all of the settings and players.
 * 
 * AUTHOR: Jonathan [modified David Code]
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

/**
 * When a client connects, a new thread is started to handle it.
 */
public class XTankServer {
	static ArrayList<DataOutputStream> sq;
	
	static String[] player1 = new String[3];
	static String[] player2 = new String[3];
	static String[] player3 = new String[3];
	static String[] player4 = new String[3];
	static int currentPlayers = 0;
	static int playerCount = 1;
	static int ready = 0;
	static Settings settings;
	private static Lock lock = new ReentrantLock();
	
	public static void main(String[] args) throws Exception {
		
		System.out.println(InetAddress.getLocalHost());
		sq = new ArrayList<>();
		
		try (var listener = new ServerSocket(59896)) {
			System.out.println("The XTank server is running...");
			var pool = Executors.newFixedThreadPool(20);
			while (true) {
				pool.execute(new XTankManager(listener.accept(), currentPlayers)); } } }

	private static class XTankManager implements Runnable {
		
		private Socket socket; 
		private int playerNum;
		Player player;

		XTankManager(Socket socket, int player) {
			this.socket = socket; this.playerNum = player + 1; currentPlayers++; }

		@Override
		public synchronized void run() {
			System.out.println("Connected: " + socket);
			try {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
				
				sq.add(out);

				if (playerNum == 1) {
					out.writeInt(1);
					settings = (Settings) inObj.readObject();
					playerCount = settings.players;
					System.out.println(settings.players); 
					System.out.println(settings.map); 
					System.out.println(settings.rules); }
				else { out.writeInt(0); }
				
				this.player = (Player) inObj.readObject();
				out.writeInt(playerNum);
				
				if (playerNum == 1) {
					player1[0] = player.getName(); player1[1] = player.getTankType(); player1[2] = player.getTankColor(); }
				else if (playerNum == 2) {
					player2[0] = player.getName(); player2[1] = player.getTankType(); player2[2] = player.getTankColor(); }
				else if (playerNum == 3) {
					player3[0] = player.getName(); player3[1] = player.getTankType(); player3[2] = player.getTankColor(); }
				else if (playerNum == 4) {
					player4[0] = player.getName(); player4[1] = player.getTankType(); player4[2] = player.getTankColor(); }
				
				// add to a bounds list
				
				ready++;
				WaitingDialog wait = new WaitingDialog();
				int leave;
				System.out.println("I am ready to write an object.");
				if (ready != playerCount) { outObj.writeObject(wait); leave = 0; }
				else { leave = 1; outObj.writeObject(null); }

				while (leave == 0) {
					if (ready == playerCount) { leave = 1; } }
				out.writeInt(playerCount);
				
				sendPlayers(outObj);

				// hey we're trying to find the best bound which we'll then send to every player
				
				int ycoord;
				while (true) {
					ycoord = in.readInt();
					//System.out.println("ycoord = " + ycoord);
					for (DataOutputStream o: sq) {
						//System.out.println("o = " + o);
						o.writeInt(ycoord); } } } 

			catch (Exception e) {
				System.out.println("Error:" + socket); }
			finally {
				try { socket.close(); } 
				catch (IOException e) {}
				System.out.println("Closed: " + socket); } }
	}
	
	public static synchronized void sendPlayers(ObjectOutputStream outObj) {
		lock.lock(); //---- Acquire the lock
		try {
			outObj.writeObject(player1);
			outObj.writeObject(player2);
			outObj.writeObject(player3);
			outObj.writeObject(player4);
			// delay to see the data-corruption problem
			Thread.sleep(5); }
		catch (InterruptedException ex) {}
		catch (IOException ex) {}
		finally {
			lock.unlock(); } // release lock
	}
}