package co.adet.sims.ui.incidents;

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
 * Incident port java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPll incident reports logged in the system, and a Dialog form
 * shown by a button to log new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is
 * shown by clicking the Incidents Button in the MainFrame's sidebar.
 * 
 * @author Elmer M. Cuenca
 *
 */

public class IncidentsManagementPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable jtblReportTable;
	
	/**
	 * Add Form dialog of this panel
	 */
	protected AddReportDialog incidentAddDialog;
	protected UpdateReportDialog incidentReportDialog;
	
	protected IncidentTableModel incidentTableModel;
	

	/**
	 * Create the panel.
	 */
	public IncidentsManagementPanel() {
		
		IncidentsManagementPanel thisPanel = this;
		
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel jpnlHeader = new JPanel();
		jpnlHeader.setBackground(Color.WHITE);
		jpnlHeader.setAlignmentY(0.0f);
		jpnlHeader.setAlignmentX(0.0f);
		jpnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
		jpnlHeader.setMaximumSize(new Dimension(32767, 150));
		add(jpnlHeader);
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
		
		/* jlblHeader - Header label */
		JLabel jlblHeader = new JLabel(" Incidents");
		jlblHeader.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
		jpnlHeader.add(jlblHeader);
		/* END OF jlblHeader */
		
		JPanel jpnlButtonActions = new JPanel();
		jpnlButtonActions.setBackground(Color.WHITE);
		jpnlButtonActions.setAlignmentX(0.0f);
		FlowLayout flowLayout = (FlowLayout) jpnlButtonActions.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		jpnlHeader.add(jpnlButtonActions);
		
		JButton jbtnAdd = new JButton("New Report");
		jbtnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnAdd.setAlignmentY(0.0f);
		jbtnAdd.addActionListener(event -> {
			incidentAddDialog.resetForm();
			incidentAddDialog.setVisible(true);
		});
		
		jpnlButtonActions.add(jbtnAdd);
		
		JButton jbtnUpdate = new JButton("Update Report");
		jbtnUpdate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblReportTable.getSelectedRow();
				if(selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select an incident first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				int databaseIdOfSelectedIncident = (int) jtblReportTable.getValueAt(selectedRowIndexOnTable, 0);
				
				incidentReportDialog.initializeWithIncidentId(databaseIdOfSelectedIncident);
				incidentReportDialog.setVisible(true);			
			}
		});
		
		jpnlButtonActions.add(jbtnUpdate);
		
		JButton jbtnDelete = new JButton("Delete");
		jbtnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblReportTable.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select an incident first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (JOptionPane.showConfirmDialog(thisPanel, 
						"Are you sure you want to delete this visitor?") == JOptionPane.YES_OPTION) {
					
					int databaseIdOfSelectedIncident = (int) jtblReportTable.getValueAt(selectedRowIndexOnTable, 0);
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
							"sims", "admin123");
							PreparedStatement deleteStatement = connection
									.prepareStatement("DELETE FROM incident_report WHERE id = ?")){
						deleteStatement.setInt(1, databaseIdOfSelectedIncident);
						deleteStatement.execute();
						
						JOptionPane.showMessageDialog(thisPanel, "Successfully deleted incident.", "Success!",
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
		jpnlButtonActions.add(jbtnDelete);
		
		JScrollPane jscrllpnReportTable = new JScrollPane();
		jscrllpnReportTable.setAlignmentY(0.0f);
		jscrllpnReportTable.setAlignmentX(0.0f);
		add(jscrllpnReportTable);
		
		jtblReportTable = new JTable();
		incidentTableModel = new IncidentTableModel();
		jtblReportTable.setModel(incidentTableModel);
		jscrllpnReportTable.setViewportView(jtblReportTable);
		
		//Create the add form dialog
		incidentAddDialog = new AddReportDialog();
		incidentAddDialog.incidentManagementPanel = this; 
		
		incidentReportDialog = new UpdateReportDialog();
		incidentReportDialog.incidentManagementPanel = this;

	}
	
	public void updateTable() {
		incidentTableModel.refresh();
	}

}
