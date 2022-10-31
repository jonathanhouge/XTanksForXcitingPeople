import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WaitingDialog {
	
	Display display;
	int cont = 0;
	
	public void start() {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(100, 100);
		
		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);

		Text wait = new Text(shell, SWT.READ_ONLY);
		wait.setText("Waiting for all players...");
		
		shell.pack(); shell.open();
	}
	
	public void close() {
		display.dispose();
	}
}
