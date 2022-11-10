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

			// if the user is the first, let them host and set everything up (send results to the server)
			int h = in.readInt();
			if (h == 1) {
				var host = new XTankHostDisplay();
				Settings hosting = host.start();
				outObj.writeObject(hosting); }
			
			// let the user create a player and give it to the server
			var create = new PlayerCreateDisplay();
			you = create.start(); outObj.writeObject(you);
			you.setPlayerNumber(in.readInt());
			
			// wait until the server gives it the go ahead
			var wait = (WaitingDialog) inObj.readObject();
			if (wait != null) { 
				((WaitingDialog) wait).start(); }
			
			int playerCount = 0;
			while (playerCount == 0) {
				playerCount = in.readInt(); }
			
			String[] p1 = (String[]) inObj.readObject();
			String[] p2 = (String[]) inObj.readObject();
			String[] p3 = (String[]) inObj.readObject();
			String[] p4 = (String[]) inObj.readObject();
			
			// bound calculation would be here
			
			// when actually getting ideal borders, won't be getting x and y from 'you'
			var ui = new XTankUI(in, out, you.getDisplayWidth(), you.getDisplayHeight(), you);
			ui.start(); }
	}
}