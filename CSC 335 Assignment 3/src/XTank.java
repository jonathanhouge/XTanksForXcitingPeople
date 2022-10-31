/*
 * 
 */

import java.net.Socket;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;

public class XTank {
	
	public static Player you;
	
	public static void main(String[] args) throws Exception {
		try (var socket = new Socket("127.0.0.1", 59896)) {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());

			int h = in.readInt();
			System.out.println("I got back: " + h);
			if (h == 1) {
				var host = new XTankHostDisplay();
				ArrayList<String> hosting = host.start();
				out.writeUTF(hosting.get(0));
				out.writeUTF(hosting.get(1));
				out.writeUTF(hosting.get(2)); }
			
			int start = 0;
			while (start == 0) {
				var create = new PlayerCreateDisplay();
				you = create.start(); outObj.writeObject(you);
				start = 1; }
			
			start = 0;
			while (start == 0) {
				start = in.readInt(); }

			var ui = new XTankUI(in, out);
			ui.start(); }
	}
}