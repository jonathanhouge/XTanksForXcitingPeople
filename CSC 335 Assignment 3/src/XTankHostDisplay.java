import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class XTankHostDisplay {

	public ArrayList<String> start() {
		Display display = new Display();
		Shell shell = new Shell (display);
		shell.setSize(100, 100);
		shell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);
		
		GridLayout buttonLayout = new GridLayout();
		Font labelFont = new Font(display, "Courier", 12, SWT.NONE);
		Font buttonFont = new Font(display, "Courier", 8, SWT.NONE);
		Color color = display.getSystemColor(SWT.COLOR_GREEN);
		
		Text title = new Text(shell, SWT.READ_ONLY);
		title.setText("You're the Host!");
		title.setFont(new Font(display, "Courier", 18, SWT.NONE));
		title.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
		
		// amount of players; default: two players
		ArrayList<String> decision1 = new ArrayList<String>(); decision1.add("2");
		playerButtons(shell, decision1, buttonLayout, labelFont, buttonFont, color);
		
		// map to play on; default: regular
		ArrayList<String> decision2 = new ArrayList<String>(); decision2.add("Regular");
		mapButtons(shell, decision2, buttonLayout, labelFont, buttonFont, color);
		
		// rules to use; default: standard
		ArrayList<String> decision3 = new ArrayList<String>(); decision3.add("Standard");
		ruleButtons(shell, decision3, buttonLayout, labelFont, buttonFont, color);
		
		// start button - will accept the current settings when clicked
		ArrayList<String> decision = new ArrayList<String>();
		Button start = new Button(shell, SWT.PUSH);
		start.setText("Host"); start.setFont(new Font(display, "Courier", 10, SWT.NONE));
		start.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		start.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		selectListenCreation(start, decision);

		shell.pack(); shell.open();
		
		while (decision.size() == 0) { // while the start button hasn't been clicked
			if (!display.readAndDispatch ())
				display.sleep (); }
		
		display.dispose();
			
		String playerCount = decision1.get(decision1.size() - 1);
		String map = decision2.get(decision2.size() - 1);
		String rules = decision3.get(decision3.size() - 1);
		ArrayList<String> decisions = new ArrayList<String>();
		decisions.add(playerCount); decisions.add(map); decisions.add(rules);
		return decisions; } // let's play!
		
		protected static void selectListenCreation(Button button, ArrayList<String> decision) {
			button.addSelectionListener(new SelectionAdapter()  {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button source =  (Button) e.getSource();
					decision.add(source.getText()); } }); }

		// radio button to determine player count (2 - 4 players)
		private static void playerButtons(Shell shell, ArrayList<String> decision, GridLayout layout, Font title, Font button, Color color) {
			Group players = new Group(shell, SWT.NONE);
			players.setLayout(layout);
			Label label = new Label(players, SWT.NONE);
			label.setText("How many Players?"); label.setFont(title); label.setForeground(color);

	        Button two = new Button(players, SWT.RADIO); two.setText("2");
	        two.setSelection(true); two.setFont(button); two.setForeground(color);
	        selectListenCreation(two, decision);
	        
	        Button three = new Button(players, SWT.RADIO); three.setText("3");
	        three.setFont(button); three.setForeground(color);
	        selectListenCreation(three, decision);

	        Button four = new Button(players, SWT.RADIO); four.setText("4");
	        four.setFont(button); four.setForeground(color);
	        selectListenCreation(four, decision); }
		
		// radio button picking map to play on
		private static void mapButtons(Shell shell, ArrayList<String> decision, GridLayout layout, Font title, Font button, Color color) {
			Group map = new Group(shell, SWT.NONE);
	        map.setLayout(layout);
	        Label label = new Label(map, SWT.NONE);
	        label.setText("On which rad Map?"); label.setFont(title); label.setForeground(color);
	        
	        Button reg = new Button(map, SWT.RADIO); reg.setText("Default"); 
	        reg.setSelection(true); reg.setFont(button); reg.setForeground(color);
	        selectListenCreation(reg, decision);

	        Button empty = new Button(map, SWT.RADIO); 
	        empty.setText("Whitespace"); empty.setFont(button); empty.setForeground(color);
	        selectListenCreation(empty, decision);
	        
	        Button maze = new Button(map, SWT.RADIO); maze.setText("Labyrinth");
	        maze.setFont(button); maze.setForeground(color);
	        selectListenCreation(maze, decision); }
		
		// radio button picking rules to use
		private static void ruleButtons(Shell shell, ArrayList<String> decision, GridLayout layout, Font title, Font button, Color color) {
			Group rules = new Group(shell, SWT.NONE);
	        rules.setLayout(layout);
	        Label label = new Label(rules, SWT.NONE);
	        label.setText("Your Rules today?"); label.setFont(title); label.setForeground(color);
	        
	        Button standard = new Button(rules, SWT.RADIO); standard.setText("Standard");
	        standard.setSelection(true); standard.setFont(button); standard.setForeground(color);
	        selectListenCreation(standard, decision);
	        
	        Button round = new Button(rules, SWT.RADIO); round.setText("Rounds");
	        round.setFont(button); round.setForeground(color);
	        selectListenCreation(round, decision); }
	}
