package co.adet.sims.app;

import javax.swing.SwingUtilities;

import co.adet.sims.ui.EntryFrame;
import co.adet.sims.ui.MainFrame;
import co.adet.sims.ui.SidebarPanel;


/**
 * PUP-SIMS Application Entry Point.<br><br>
 * 
 * Creates all main application objects here and wires them together.
 * 
 * @author 
 *
 */
public class SimsApplication {
	
	public static void main(String[] args) {
	
		MainFrame mainFrame = new MainFrame();
		EntryFrame entryFrame = new EntryFrame();
		entryFrame.setAdministratorEntryPoint(mainFrame);
	
		SwingUtilities.invokeLater(() -> entryFrame.setVisible(true));
	}

}
