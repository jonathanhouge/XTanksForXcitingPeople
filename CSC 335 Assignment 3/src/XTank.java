/*
 * 
 */

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class XTank {
	public static void main(String[] args) throws Exception {
		try (var socket = new Socket("127.0.0.1", 59896)) {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			int start = 0;
			if (in.readInt() == 0) {
				var host = new XTankHostDisplay();
				while (start == 0) {
					start = host.start(); } 
			}
			
			start = 0;
			while (start == 0) {
				var create = new PlayerCreateDisplay();
				start = create.start(); }
			
			out.writeInt(1);
			var ui = new XTankUI(in, out);
			ui.start(); }
	}
}