import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class XTankGameOverDisplay {
	
	public void start(boolean isAlive) {
			
		Display display = new Display(); 
		Shell shell = new Shell (display);
		shell.setSize(100, 100);

		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);

		// end of game message
		Text title = new Text(shell, SWT.READ_ONLY);
		title.setText("The game is over! One or less players are alive!");
		
		Text result = new Text(shell, SWT.READ_ONLY);
		if (isAlive) {
			result.setText("Hey! You won, congratulations!"); }
		else {
			result.setText("Better luck next time!"); }

		ArrayList<String> decision = new ArrayList<String>();
        Button end = new Button(shell, SWT.PUSH | SWT.CENTER); end.setText("Acknowledged!");
        selectListenCreation(end, decision);

		shell.pack(); shell.open();

		while (decision.size() == 0) { // when the user clicks exit, the display closes
			if (!display.readAndDispatch ())
				display.sleep (); }
		display.dispose(); }
	
		// selection listener - for the radio buttons & submit button
		protected static void selectListenCreation(Button button, ArrayList<String> decision) {
			button.addSelectionListener(new SelectionAdapter()  {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button source =  (Button) e.getSource();
					decision.add(source.getText()); } }); }
}