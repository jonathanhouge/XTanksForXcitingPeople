/* The server object. One of the players will need to run this code and, after, they
 * run client (XTank). This holds onto all of the settings and players.
 */

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.Executors;

/**
 * When a client connects, a new thread is started to handle it.
 */
public class XTankServer {
	static ArrayList<DataOutputStream> sq;
	static int currentPlayers = 0;
	static int playerCount = 1;
	static int ready = 0;
	static String map;
	static String rules;
	static Settings settings;
	
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
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
				
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
				
				ready++;
				WaitingDialog wait = new WaitingDialog();
				int leave;
				if (ready != playerCount) { wait.start(); leave = 0; }
				else { leave = 1; }

				while (leave == 0) {
					if (ready == playerCount) { leave = 1; } }
				out.writeInt(1);
				
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
}