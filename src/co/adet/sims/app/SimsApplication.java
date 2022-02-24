package co.adet.sims.app;

import javax.swing.SwingUtilities;

import co.adet.sims.ui.EntryFrame;
import co.adet.sims.ui.MainFrame;
import co.adet.sims.ui.SidebarPanel;
import co.adet.sims.ui.securityguards.SecurityManagementPanel;

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
		//
		SecurityManagementPanel securityManagementPanel = new SecurityManagementPanel();
		
		MainFrame mainFrame = new MainFrame();
		SidebarPanel sidebarPanel = new SidebarPanel();
		mainFrame.setSecurityManagementPanel(securityManagementPanel);
		
		
		EntryFrame entryFrame = new EntryFrame();
		entryFrame.setAdministratorEntryPoint(mainFrame);
		entryFrame.setAdministratorSideBar(sidebarPanel);
		
		SwingUtilities.invokeLater(() -> entryFrame.setVisible(true));
	}

}
