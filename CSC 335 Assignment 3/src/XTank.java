/* The client object. Every person who intends to play the game needs to run this code.
 * It sets the user up by creating a player object with their desired preferences and
 * will even let the first user create the settings the game will follow. 
 * 
 * AUTHOR: Jonathan [modified David code]
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

			int h = in.readInt();
			if (h == 1) {							// Check to see if this is the first player
				var host = new XTankHostDisplay(); 	// Because this is the 1st player, create a XTankHostDisplay
				Settings hosting = host.start(); 	// Settings will now be created
				outObj.writeObject(hosting); 		// Send the settings to the server
			}
			
			// Tank creation display begins!
			var create = new PlayerCreateDisplay(); // open a PlayerCreateDisplay window
			you = create.start(h); 					// This now has the player object!
			outObj.writeObject(you); 				// player sent to server

			// Either a waiting window is recieved and started or not
			var wait = inObj.readObject(); 			
			if (wait != null) { 					// null check because waiting window isn't created for last player
				((WaitingDialog) wait).start(); 	
			}

			// This block essentially waits for all players to be made. Server notifys when ready.
			int start = 0; 							
			while (start == 0) { 
				start = in.readInt();
			}

			var playersObj = inObj.readObject(); // Manager has received playerArray but as an object
			playerArr = (Player[]) playersObj; // Convert to playerArray

			var ui = new XTankUI(in, out, you.getDisplayWidth(), you.getDisplayHeight(), h, playerArr);
			ui.start(); // start UI thread!

		}
	}
}