/*
 * 
 */

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class XTank {
	public static void main(String[] args) throws Exception {
		//XTankServer.main(null);
		try (var socket = new Socket("127.0.0.1", 59896)) {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
<<<<<<< HEAD
//			int start = 0;
//			if (in.readInt() == 0) {
//				var host = new XTankHostDisplay();
//				while (start == 0) {
//					start = host.start(); } 
//			}
//			
//			start = 0;
//			while (start == 0) {
//				var create = new PlayerCreateDisplay();
//				start = create.start(); }
			
			out.writeInt(1);
=======
			//int start = 0;
//			if (in.readInt() == 0) {
//				var host = new XTankHostDisplay();
//				while (start == 0) {
//					start = host.start(); } 
//			}
//			
//			start = 0;
//			while (start == 0) {
//				var create = new PlayerCreateDisplay();
//				start = create.start(); }
//			
//			out.writeInt(1);
>>>>>>> branch 'RotationTesting' of https://github.com/jonathanhouge/XTanksForXcitingPeople.git
			var ui = new XTankUI(in, out);
			ui.start(); }
	}
}