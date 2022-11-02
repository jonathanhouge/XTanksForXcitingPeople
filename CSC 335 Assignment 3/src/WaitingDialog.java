/* The Waiting Display.
 * The host declares how many players are playing. After a player is created,
 * a global 'ready' variable is incremented. If the amount of players isn't
 * equal to the amount of players ready, this waiting dialog appears for a few
 * moments, just to let the client know that the actual game will start
 * when the right amount of players have joined. 
 */

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;

public class WaitingDialog {

	public void start() throws InterruptedException {
		//-- setting up
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(100, 100);
		
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);

		// the text that'll just let the player know that the game is waiting
		Text wait = new Text(shell, SWT.READ_ONLY);
		wait.setText("Waiting for all players...");
		
		shell.pack(); shell.open();
		
		// times - we want the dialog to stop when now reaches then
		long then = System.currentTimeMillis() +  TimeUnit.SECONDS.toMillis(3);
		long now = System.currentTimeMillis();
		
		while (now < then) { // wait two seconds and then close the dialog
			now = System.currentTimeMillis();
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		display.dispose();
	}
}