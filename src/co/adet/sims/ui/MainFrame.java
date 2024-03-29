package co.adet.sims.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main Frame of the Application. Every interaction possible for
 * this data-entry system is made in here.
 * 
 * Has a side navigator which dictates what current panel to
 * view, and a content panel.
 * 
 *
 */
public class MainFrame extends JFrame {
	
	/**
	 * Default Serial Version UID (for serializability, not important, placed to remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Main Content Pane of this Frame
	 */
	private JPanel jpnlContentPane;
	
	/**
	 * Current shown panel.
	 */
	private JPanel jpnlCurrentShownPanel;
	
	/**
	 * Attendance management panel.
	 */
	
	protected co.adet.sims.ui.attendance.ManagementPanel attendanceManagementPanel;
	
	
	/**
	 * Violation management panel.
	 */
	 
	protected co.adet.sims.ui.violation.ViolationManagementPanel violationManagementPanel;
	
	
	/**
	 * Inpsection management panel.
	 */
	 
	protected co.adet.sims.ui.inspection.InspectionManagementPanel inspectionManagementPanel;
	
	
	/**
	 * Parking slot management panel.
	  */
	
	protected co.adet.sims.ui.parking.ParkingManagementPanel parkingManagementPanel;
	
	/**
	 * Visitor Log management panel.
	 */
	 
	protected co.adet.sims.ui.visitor.VisitorManagementPanel visitorManagementPanel;
	
	/**
	 * Car Log management panel.
	 */
	 
	protected co.adet.sims.ui.car.CarManagementPanel carManagementPanel;
	
	
	/**
	 * Security Guard Panel 
	*/
	protected co.adet.sims.ui.securityguards.SecurityManagementPanel securityGuardManagementPanel; 
	 
	/**
	 * Incident Report Panel 
	 */
	 
	protected co.adet.sims.ui.incidents.IncidentsManagementPanel incidentManagementPanel; 
	
	/**
	 * Inventory Of Supplies Panel 
	 */
	
	protected co.adet.sims.ui.inventory.InventoryManagementPanel inventoryManagementPanel;
	
	
	/**
	 * Construct the frame.
	 */
	
	public MainFrame() {
		
		// Set the applicable sizes
		setMinimumSize(new Dimension(800, 550));
		setPreferredSize(new Dimension(800, 550));
		
		// Set main background to white
		setBackground(Color.WHITE);
		
		// Set title
		setTitle("Security Information Management System");
		
		// When this frame is closed, the JFrame gets dumped
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set location to middle of screen
		setLocationRelativeTo(null);
		
		// Create the main content pane of this frame
		jpnlContentPane = new JPanel();
		jpnlContentPane.setBackground(Color.WHITE);
		jpnlContentPane.setBorder(null);
		
		// Set the content pane of this frame to the custom JPanel
		setContentPane(jpnlContentPane);
		
		// Use BoxLayout to:
		// 1. Layout the Sidebar and Main Panel (JPanel) on one direction (Line-Axis)
		// 2. So that the Sidebar and Main Panel's sizes are respected by this layout manager
		jpnlContentPane.setLayout(new BoxLayout(jpnlContentPane, BoxLayout.LINE_AXIS));
		
		// Create the Sidebar Panel
		SidebarPanel sidebarPanel = new SidebarPanel();
		// Set the Sidebar Panel hook to this MainFrame
		sidebarPanel.setMainFrame(this);
		// Add the Sidebar to the main sole pane of the frame
		jpnlContentPane.add(sidebarPanel);
		
		
		
		// Create the Violation Management Panel
		violationManagementPanel = new co.adet.sims.ui.violation.ViolationManagementPanel();
		
		// Create the Building Inspection Management Panel
		inspectionManagementPanel = new co.adet.sims.ui.inspection.InspectionManagementPanel();
		
		// Create the Parking Slot Management Panel
		parkingManagementPanel = new co.adet.sims.ui.parking.ParkingManagementPanel();
		
		// Create the Incident Report Management Panel 
		incidentManagementPanel = new co.adet.sims.ui.incidents.IncidentsManagementPanel();
		// Create the Inventory of Supplies Management Panel
		inventoryManagementPanel = new co.adet.sims.ui.inventory.InventoryManagementPanel();
		
		//Create the Visitor Management Panel
		visitorManagementPanel = new co.adet.sims.ui.visitor.VisitorManagementPanel();

		// Create the Security Guard Management Panel 
		securityGuardManagementPanel = new co.adet.sims.ui.securityguards.SecurityManagementPanel(); 
		
		// Create the Attendance Management Panel
		attendanceManagementPanel = new co.adet.sims.ui.attendance.ManagementPanel();
		
		//Create the Car Management Panel
		carManagementPanel = new co.adet.sims.ui.car.CarManagementPanel();
	}
	
	/**
	 * Show the attendance management panel.
	 */
	 
	public void showAttendanceManagementPanel() {
		// If another panel is shown, remove it from the content pane
		if(jpnlCurrentShownPanel != null) {
			if(jpnlCurrentShownPanel == attendanceManagementPanel)
				return;
			jpnlContentPane.remove(jpnlCurrentShownPanel);
		}
		
		// Add the attendance management panel on the content pane
		jpnlCurrentShownPanel = attendanceManagementPanel;
		jpnlContentPane.add(attendanceManagementPanel);
		
		// Prompt revalidation of the containment hierarchy
		// since we dynamically added a new component
		// while this mainframe is shown.
		revalidate();
		repaint();
		
	}
	
	
	/**
	 * Show the violation management panel.
	 	*/
	 
	public void showViolationManagementPanel() {
		// If the current shown panel is the violation management panel, return.
		if(jpnlCurrentShownPanel == violationManagementPanel)
			return;
		
		// Else if the current shown panel is not,
		// then remove it from the panel
		else if(jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel);
		
		// Set the violation management panel as the new panel
		jpnlCurrentShownPanel = violationManagementPanel;
		jpnlContentPane.add(violationManagementPanel);
		
		// Redraw the whole frame
		revalidate();
		repaint();
	}

	
	/**
	 * Show the building inspection management panel.
	 */
	
	public void showInspectionManagementPanel() {
		// If the current shown panel is the inspection management panel, return.
		if(jpnlCurrentShownPanel == inspectionManagementPanel)
			return;
		
		// Else if the current shown panel is not,
		// then remove it from the panel
		else if(jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel);
		
		// Set the inspection management panel as the new panel
		jpnlCurrentShownPanel = inspectionManagementPanel;
		jpnlContentPane.add(inspectionManagementPanel);
		
		// Redraw the whole frame
		revalidate();
		repaint();
	}
	 
	
	
	/**
	 * Show the parking slot management panel.
	 */
	
	public void showParkingSlotManagementPanel() {
		// If the current shown panel is the parking slot management panel, return.
		if(jpnlCurrentShownPanel == parkingManagementPanel)
			return;
		
		// Else if the current shown panel is not,
		// then remove it from the panel
		else if(jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel);

		// Refresh the table
		parkingManagementPanel.updateTable();
		// Set the parking slot management panel as the new panel
		jpnlCurrentShownPanel = parkingManagementPanel;
		jpnlContentPane.add(parkingManagementPanel);
		
		// Redraw the whole frame
		revalidate();
		repaint();
	}
	
	
	/**
	 * Show the visitor log management panel.
	 */
	
	public void showVisitorLogManagementPanel() {
		// If the current shown panel is the visitor log management panel, return.
		if(jpnlCurrentShownPanel == visitorManagementPanel)
			return;
		
		// Else if the current shown panel is not,
		// then remove it from the panel
		else if(jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel);
		
		// Refresh the table
		visitorManagementPanel.updateTable();
		// Set the visitor log management panel as the new panel
		jpnlCurrentShownPanel = visitorManagementPanel;
		jpnlContentPane.add(visitorManagementPanel);
		
		// Redraw the whole frame
		revalidate();
		repaint();
	}
	
	/**
	 * Show the visitor log management panel.
	 */
	
	public void showCarManagementPanel() {
		// If the current shown panel is the car management panel, return.
		if(jpnlCurrentShownPanel == carManagementPanel)
			return;
		
		// Else if the current shown panel is not,
		// then remove it from the panel
		else if(jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel);
		
		// Refresh the table
		carManagementPanel.updateTable();
		// Set the car management panel as the new panel
		jpnlCurrentShownPanel = carManagementPanel;
		jpnlContentPane.add(carManagementPanel);
		
		// Redraw the whole frame
		revalidate();
		repaint();
	}
	
	
	
	
	
	/**
	 * Show the security guard management panel. 
	 */
	
	public void showSecurityGuardManagementPanel() {
		// Iff the current shown panel is the security guard management panel, do nothing
		if (jpnlCurrentShownPanel == securityGuardManagementPanel)
			return; 
		
		// Else iff the current shown panel is not,
		// then remove it from the panel
		else if (jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel); 
		
		// Refresh the table
		securityGuardManagementPanel.updateTable();
		// Set the security guard management panel as the new panel
		jpnlCurrentShownPanel = securityGuardManagementPanel; 
		jpnlContentPane.add(securityGuardManagementPanel);
		
		//Redraw the whole frame
		revalidate();
		repaint();
	}
	 
	
	/**
	 * Show the incident management panel. 
	 */
	
	public void showIncidentManagementPanel() {
		// Iff the current shown panel is the incident management panel, do nothing 
		if (jpnlCurrentShownPanel == securityGuardManagementPanel)
			return;
		
		// Else iff the current shown panel is not,
		// then remove it from the panel
		else if (jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel);
		
		// Refresh the table
		incidentManagementPanel.updateTable();
		// Set the incident report management panel as the new panel 
		jpnlCurrentShownPanel = incidentManagementPanel; 
		jpnlContentPane.add(incidentManagementPanel); 
		
		// Redraw the whole frame
		revalidate();
		repaint();
	}
	
	
	/**
	 * Show the inventory of supplies management panel.
	 */
	 
	public void showInventoryManagementPanel() {
		// Iff the current shown panel is the inventory management panel, do nothing
		if (jpnlCurrentShownPanel == inventoryManagementPanel)
			return;
		
		// Else iff the current shown panel is not,
		// then remove it from the panel 
		else if (jpnlCurrentShownPanel != null)
			jpnlContentPane.remove(jpnlCurrentShownPanel);
		
		// Refresh the table
		inventoryManagementPanel.updateTable();
		// Set the inventory management panel as the new panel
		jpnlCurrentShownPanel = inventoryManagementPanel; 
		jpnlContentPane.add(inventoryManagementPanel);
		
		
		// Redraw the whole frame
		revalidate();
		repaint();
	}
}


