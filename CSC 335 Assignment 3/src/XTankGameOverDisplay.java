/* Game Over Display.
 * Only appears in Standard mode. Displays whenever there's one or less players alive.
 * Informs that the game is over, telling the client if they won or lost.
 * Two text options: 
 * 		"Let's play again!" starts the game again. Needs to be clicked twice. Only works properly for one more play again.
 * 		"Acknowledged. I'm outta here." closes the dialog, XTank (client) instance is terminated.
 * 
 * Uses XTankHostDisplay's selectListenCreation to save code.
 * 
 * AUTHOR: Jonathan Houge
 */

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;

public class XTankGameOverDisplay {
	
	// start - takes boolean to determine if message is for winner or loser. returns whether another game shall be played.
	public boolean start(boolean isAlive) {
		
		//-- create and set up the display
		Display display = new Display(); 
		Shell shell = new Shell (display);
		shell.setSize(100, 100);

		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);

		// end of game message
		Text title = new Text(shell, SWT.READ_ONLY);
		title.setText("The game is over! One or less players are alive!");
		
		// end of game customed message - you won or not
		Text result = new Text(shell, SWT.READ_ONLY);
		if (isAlive) {
			result.setText("Hey! You won, congratulations!"); }
		else {
			result.setText("Better luck next time!"); }

		//-- do you want to play again?
		
		//yes!
		ArrayList<String> decision = new ArrayList<String>();
        Button again = new Button(shell, SWT.PUSH | SWT.CENTER); again.setText("Let's play again!");
        XTankHostDisplay.selectListenCreation(again, decision);
        
        // no!
        Button end = new Button(shell, SWT.PUSH | SWT.CENTER); end.setText("Acknowledged. I'm outta here.");
        XTankHostDisplay.selectListenCreation(end, decision);

		shell.pack(); shell.open();

		while (decision.size() == 0) { // when the user clicks an option, the display closes
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		// default not playing again - if they want to, let's return true
		boolean playAgain = false;
		if (decision.get(decision.size() -1).equals("Let's play again!")) {
			playAgain = true; }
		
		display.dispose();
		return playAgain; }
}