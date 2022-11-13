/* The client object. Every person who intends to play the game needs to run this code.
 * It sets the user up by creating a player object with their desired preferences and
 * will even let the first user create the settings the game will follow. 
 * 
 * AUTHOR: Jonathan Houge [modified David code]
 */

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class XTank {

	public static Player you;
	public static volatile Player[] playerArr;

	public static void main(String[] args) throws Exception {
		try (var socket = new Socket("127.0.0.1", 59896)) { // will manually have to change IP (set to self right now)
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());

			// first player gets to host
			int num = in.readInt();
			if (num == 1) {							// check to see if this is the first player
				var host = new XTankHostDisplay();
				Settings hosting = host.start(); 	// host display ran, will create settings and return
				outObj.writeObject(hosting); }		// send settings to server
			
			// get Settings from the server - syncs map and rules b/w clients
			Settings hostChosen = (Settings) inObj.readObject();
			
			// player creation
			var create = new PlayerCreateDisplay();
			you = create.start(num); 					// player display ran, will create player and return
			outObj.writeObject(you); 				// player sent to server

			// waiting dialog is given (all players except the last player receive one)
			var wait = inObj.readObject(); 			
			if (wait != null) { 					// last player gets null
				((WaitingDialog) wait).start(); }

			// waits for server to notify clients that all players have joined
			int start = 0; 							
			while (start == 0) { 
				start = in.readInt(); }

			var playersObj = inObj.readObject(); // get player array from server
			playerArr = (Player[]) playersObj;

			XTankUI ui = new XTankUI(in, out, you.getDisplayWidth(), you.getDisplayHeight(), num, playerArr, hostChosen);
			ui.start(); } } // start UI, let's play!
}