package co.adet.sims.ui.parking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

/**
 * Parking Slot Management Panel of this Application. Contains a Table
 * showing parking slot status, and a Dialog shown by a button to
 * create new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is
 * shown by clicking the Parking Slot Button in the MainFrame's sidebar.
 * 
 * @author Bismillah C. Constantino
 *
 */
public class ParkingManagementPanel extends JPanel {
	
	ParkingManagementPanel thisPanel = this;

	/**
	 * Default Serial Version UID (for serializability, not important, placed to remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The main table of this panel.
	 */
	private JTable jtblVisitorLog;
	
	/**
	 * Add Form Dialog of this panel.
	 */
	protected AddDialog parkingAddDialog;
	protected UpdateDialog parkingUpdateDialog;
	private JTable jtblParking;
	
	protected ParkingSlotTableModel parkingSlotTableModel;
	
	/**
	 * Construct the panel.
	 */
	public ParkingManagementPanel() {
		// Set background to white
		setBackground(Color.WHITE);
		// Set border to EmptyBorder for spacing
		setBorder(new EmptyBorder(10, 10, 10, 10));
		// Use BoxLayout to lay the internal 3 panels: Header, Table, Pagination Actions
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		/* jpnlHeader - Header Panel */
		JPanel jpnlHeader = new JPanel();
		jpnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
		jpnlHeader.setMinimumSize(new Dimension(10, 45));
		jpnlHeader.setMaximumSize(new Dimension(32767, 55));
		jpnlHeader.setBackground(Color.WHITE);
		add(jpnlHeader);
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
		/* END OF jpnlHeader */
		
		/* jlblHeader - Header label */
		JLabel jlblHeader = new JLabel("Manage Parking Slot");
		jlblHeader.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
		jpnlHeader.add(jlblHeader);
		/* END OF jlblHeader */

		/* jpnlButtonActions - panel for buttons */
		JPanel jpnlButtonActions = new JPanel();
		FlowLayout fl_jpnlButtonActions = (FlowLayout) jpnlButtonActions.getLayout();
		fl_jpnlButtonActions.setAlignment(FlowLayout.RIGHT);
		jpnlButtonActions.setBackground(Color.WHITE);
		jpnlHeader.add(jpnlButtonActions);
		/* END OF jpnlButtonActions */

		/* jbtnShowAddForm - button for parking slot */
		JButton jbtnShowAddForm = new JButton("Add");
		jbtnShowAddForm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnShowAddForm.addActionListener(event -> {
			parkingAddDialog.resetForm();
			parkingAddDialog.setVisible(true);
		});
		jpnlButtonActions.add(jbtnShowAddForm);
		
		JButton jbtnShowUpdateForm = new JButton("Update");
		jbtnShowUpdateForm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnShowUpdateForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblParking.getSelectedRow();
				if(selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select a parking first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				String databaseIdOfSelectedParking = (String) jtblParking.getValueAt(selectedRowIndexOnTable, 0);
				
				parkingUpdateDialog.initializeWithSlotNumber(databaseIdOfSelectedParking);
				parkingUpdateDialog.setVisible(true);
				
				
			}
		});
		jpnlButtonActions.add(jbtnShowUpdateForm);
		
		JButton jbtnShowDeleteForm = new JButton("Delete");
		jbtnShowDeleteForm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnShowDeleteForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblParking.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select a member first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (JOptionPane.showConfirmDialog(thisPanel, 
						"Are you sure you want to delete this visitor?") == JOptionPane.YES_OPTION) {
					
					String databaseIdOfSelectedParking = (String) jtblParking.getValueAt(selectedRowIndexOnTable, 0);
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
							"sims", "admin123");
							PreparedStatement deleteStatement = connection
									.prepareStatement("DELETE FROM parking_slot WHERE slot_number = ?")){
						deleteStatement.setString(1, databaseIdOfSelectedParking);
						deleteStatement.execute();
						
						JOptionPane.showMessageDialog(thisPanel, "Successfully deleted parking.", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
						updateTable();
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(thisPanel, 
								"An error occured while fetching members from the database. \n\nDetails: " 
						+ e1.getMessage());
					}
							
				}
			}
		});
		jpnlButtonActions.add(jbtnShowDeleteForm);
		/* END OF jbtnShowAddForm */
		
		/* jscrlpnParkingTable - Scrollable Table Panel */
		JScrollPane jscrlpnParkingTable = new JScrollPane();
		add(jscrlpnParkingTable);
		/* END OF jscrlpnParkingTable */
		
		/* jtblParking - Main Panel Table */
		jtblParking = new JTable();
		parkingSlotTableModel = new ParkingSlotTableModel();
		jtblParking.setModel(parkingSlotTableModel);
		jtblParking.getColumnModel().getColumn(0).setPreferredWidth(20);
		jtblParking.getColumnModel().getColumn(1).setPreferredWidth(130);
		jtblParking.getColumnModel().getColumn(2).setPreferredWidth(124);
		jtblParking.setRowHeight(25);
		jtblParking.setIntercellSpacing(new Dimension(10, 10));
		jtblParking.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jscrlpnParkingTable.setViewportView(jtblParking);
			/* END OF jtblParking */
		
		/* jtblVisitorLog - Main Panel Table */
		jtblVisitorLog = new JTable();
		jtblVisitorLog.setRowHeight(25);
		jtblVisitorLog.setIntercellSpacing(new Dimension(10, 10));
		jtblVisitorLog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		/* END OF jscrlpnParkingTable */
		
		// Create the add form dialog
		parkingAddDialog = new AddDialog();
		parkingAddDialog.parkingManagementPanel = this;
		
		parkingUpdateDialog = new UpdateDialog();
		parkingUpdateDialog.parkingManagementPanel = this;
	}
	
	public void updateTable() {
		parkingSlotTableModel.refresh();
	}

}

