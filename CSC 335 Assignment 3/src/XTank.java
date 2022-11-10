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

	public static void main(String[] args) throws Exception {
		try (var socket = new Socket("127.0.0.1", 59896)) { // will manually have to change IP (set to self right now)
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());

			int h = in.readInt();
			//System.out.println(socket + "has recieved h as : " + h);
			if (h == 1) {									// Check to see if this is the first player
				var host = new XTankHostDisplay();			// Because this is the 1st player, create a XTankHostDisplay
				Settings hosting = host.start();			// Settings will now be created
				outObj.writeObject(hosting);				// Send the settings to the server 
			}

															// Tank creation display begins!
			var create = new PlayerCreateDisplay();			// open a PlayerCreateDisplay window
			you = create.start();							// This now has the player object!
			outObj.writeObject(you);						// player sent to server

			var wait = inObj.readObject();					// Either a waiting window is recieved and started or not
			//System.out.println(socket + "has recieved the wait object : " + wait);
			if (wait != null) {								// null check because waiting window isn't created for last player
				((WaitingDialog) wait).start();				// Simply opens window
			}

			int start = 0;									// This block essentially halts the program until
			while (start == 0) {							// all players have been made. The server tells you when
				//System.out.println(socket + "is waiting (before readInt)!");
				start = in.readInt();
				//System.out.println(socket + "(after readInt) has recieved the int " + start);
			}
			//System.out.println("About to GET the player list!");

			var playerObj = inObj.readObject();
			Player[] playerArr = (Player[]) playerObj;
			int i = 0;
			System.out.println("Recieved playerArray: " + playerObj + " Debug print contents to check if one is older than the other/corrupted");
			for(Player x: playerArr) {
				i++;
				System.out.println("Player " + i + ':' + x);
			}
			if(start!= 0) {
				//System.out.println(socket + "is going to start because the start value is now: " + start);
				var ui = new XTankUI(in, out, you.getDisplayWidth(), you.getDisplayHeight(), you,playerArr);
				ui.start();
			}

			

		}
	}
}