import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class PlayerCreateDisplay {
	
	private Canvas canvas;
	private Display display;
	
	DataInputStream in; 
	DataOutputStream out;
	
	public PlayerCreateDisplay() {}

		public static int start() {
			Display display = new Display();
			Shell shell = new Shell (display);
			shell.setSize(100, 100);

			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 1;
			shell.setLayout(gridLayout);
	        
	        // pick amount of players; default: two players
			ArrayList<String> decision1 = new ArrayList<String>(); decision1.add("2");
			playerButtons(shell, decision1);
			
	        // pick deck to use; default: fruit deck
	        ArrayList<String> decision2 = new ArrayList<String>(); decision2.add("Fruits");
	        deckButtons(shell, decision2);
	        
	        // pick rules to use; default: standard rules
	        ArrayList<String> decision3 = new ArrayList<String>(); decision3.add("Standard");
	        ruleButtons(shell, decision3);
	        
	        // start button - will accept the current settings when clicked
	        ArrayList<String> decision = new ArrayList<String>();
	        Button start = new Button(shell, SWT.PUSH); start.setText("Start the Game!");
	        selectListenCreation(start, decision);

			shell.pack(); shell.open();

			while (decision.size() == 0) { // while the start button hasn't been clicked
				if (!display.readAndDispatch ())
					display.sleep (); }
			
			display.dispose();
			
			int playerCount = Integer.parseInt(decision1.get(decision1.size() - 1));
			String deck = decision2.get(decision2.size() - 1);
			String rules = decision3.get(decision3.size() - 1);
			return 1; } // let's play!
		
		protected static void selectListenCreation(Button button, ArrayList<String> decision) {
			button.addSelectionListener(new SelectionAdapter()  {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button source =  (Button) e.getSource();
					decision.add(source.getText()); } }); }

		// radio button to determine player count
		private static void playerButtons(Shell shell, ArrayList<String> decision) {
			Group players = new Group(shell, SWT.NONE);
			players.setLayout(new GridLayout());
			Label label = new Label(players, SWT.NONE);
			label.setText("Number of Players: ");

			Button one = new Button(players, SWT.RADIO); one.setText("1");
			selectListenCreation(one, decision);

	        Button two = new Button(players, SWT.RADIO); two.setText("2"); 
	        two.setSelection(true);
	        selectListenCreation(two, decision);
	        
	        Button three = new Button(players, SWT.RADIO); three.setText("3");
	        selectListenCreation(three, decision);

	        Button four = new Button(players, SWT.RADIO); four.setText("4");
	        selectListenCreation(four, decision); }
		
		// radio button picking deck to use
		private static void deckButtons(Shell shell, ArrayList<String> decision) {
			Group deck = new Group(shell, SWT.NONE);
	        deck.setLayout(new GridLayout());
	        Label label = new Label(deck, SWT.NONE);
	        label.setText("Deck to Play With: ");
	        
	        Button fruit = new Button(deck, SWT.RADIO); fruit.setText("Fruits"); 
	        fruit.setSelection(true);
	        selectListenCreation(fruit, decision);

	        Button number = new Button(deck, SWT.RADIO); number.setText("Numbers (1-9)");
	        selectListenCreation(number, decision);
	        
	        Button weezer = new Button(deck, SWT.RADIO); weezer.setText("Weezer Albums");
	        selectListenCreation(weezer, decision); }
		
		// radio button picking rules to use
		private static void ruleButtons(Shell shell, ArrayList<String> decision) {
			Group rules = new Group(shell, SWT.NONE);
	        rules.setLayout(new GridLayout());
	        Label label = new Label(rules, SWT.NONE);
	        label.setText("Rules to Play With: ");
	        
	        Button standard = new Button(rules, SWT.RADIO); standard.setText("Standard"); 
	        standard.setSelection(true);
	        selectListenCreation(standard, decision);
	        
	        Button oneFlip = new Button(rules, SWT.RADIO); oneFlip.setText("One Flip");
	        selectListenCreation(oneFlip, decision); }
	}
