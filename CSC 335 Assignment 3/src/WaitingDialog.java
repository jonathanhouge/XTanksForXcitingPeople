/* The Waiting Display.
 * The host declares how many players are playing. After a player is created,
 * a global 'ready' variable is incremented. If the amount of players isn't
 * equal to the amount of players ready, this waiting dialog appears, just to
 * let the client know that the actual game will start when the right amount 
 * of players have joined. Should be closed ASAP.
 * 
 * Uses XTankHostDisplay's selectListenCreation to save code.
 * 
 * AUTHOR: Jonathan Houge
 */

import java.util.ArrayList;
import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;

public class WaitingDialog implements Serializable {

	private static final long serialVersionUID = 1L; // to appease the java gods

	public void start() {
		
		//-- create and set up the display
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(100, 100);
		
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);

		// the text that'll just let the player know that the game is waiting
		Text wait = new Text(shell, SWT.READ_ONLY);
		wait.setText("Waiting for all players...");
		
		ArrayList<String> decision = new ArrayList<String>();
        Button end = new Button(shell, SWT.PUSH | SWT.CENTER); end.setText("Okay!");
        XTankHostDisplay.selectListenCreation(end, decision);
		
		shell.pack(); shell.open();
		
		while (decision.size() == 0) { // when the user clicks okay, the display closes
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		display.dispose();
	}
}