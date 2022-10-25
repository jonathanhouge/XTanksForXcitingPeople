/*
 * 
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * When a client connects, a new thread is started to handle it.
 */
public class XTankServer {
	static ArrayList<DataOutputStream> sq;
	
	public static void main(String[] args) throws Exception {
		System.out.println(InetAddress.getLocalHost());
		sq = new ArrayList<>();
		
		try (var listener = new ServerSocket(59896)) {
			System.out.println("The XTank server is running...");
			var pool = Executors.newFixedThreadPool(20);
			while (true) {
				pool.execute(new XTankManager(listener.accept())); } } }

	private static class XTankManager implements Runnable {
		
		private Socket socket;

		XTankManager(Socket socket) { this.socket = socket; }

		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				sq.add(out);
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