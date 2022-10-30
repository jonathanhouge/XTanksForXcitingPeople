/*
 * 
 */

import java.net.Socket;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class XTank {
	public static void main(String[] args) throws Exception {
		try (var socket = new Socket("127.0.0.1", 59896)) {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			int start;
			if (in.readInt() == 1) {
				var host = new XTankHostDisplay();
				ArrayList<String> hosting = host.start();
				out.writeUTF(hosting.get(0));
				out.writeUTF(hosting.get(1));
				out.writeUTF(hosting.get(2)); }
			
			start = 0;
			while (start == 0) {
				var create = new PlayerCreateDisplay();
				start = create.start(); }
			
			out.writeInt(1);
			var ui = new XTankUI(in, out);
			ui.start(); }
	}
}