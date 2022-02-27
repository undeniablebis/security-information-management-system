package co.adet.sims.ui.inspection;

import java.awt.Color;
import java.awt.Cursor;
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

import co.adet.sims.ui.inspection.UpdateDialog;
import co.adet.sims.ui.inspection.InspectionManagementPanel;

/**
 * Building Inspection Management Panel of this Application. Contains a Table
 * showing all logged building inspection each night, and a Dialog
 * shown by a button to create new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is
 * shown by clicking the Inspection Button in the MainFrame's sidebar.
 * 
 * @author Rian Carlo Reyes
 *
 */
public class InspectionManagementPanel extends JPanel {
	
	/**
	 * Default Serial Version UID (for serializability, not important, placed to remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The main table of this panel.
	 */
	private JTable jtblInspection;
	
	/**
	 * Add Form Dialog of this panel.
	 */
	protected AddDialog inspectionAddDialog;
	protected UpdateDialog inspectionUpdateDialog;

	
	/**
	 * Table Model of the main table.
	 */
	protected InspectionTableModel inspectionTableModel;

	/**
	 * Construct the panel.
	 */
	public InspectionManagementPanel() {
		
		InspectionManagementPanel thisPanel = this;
		
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
		/* END OF jpnlHeader */
		
		/* jlblHeader - Main Header Label */
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
		JLabel jlblHeader = new JLabel("Manage Inspection Log");
		jlblHeader.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
		jpnlHeader.add(jlblHeader);
		/* END OF jlblHeader */

		/* jpnlButtonActions - Header Panel Buttons */
		JPanel jpnlButtonActions = new JPanel();
		FlowLayout fl_jpnlButtonActions = (FlowLayout) jpnlButtonActions.getLayout();
		fl_jpnlButtonActions.setAlignment(FlowLayout.RIGHT);
		jpnlButtonActions.setBackground(Color.WHITE);
		jpnlHeader.add(jpnlButtonActions);
		/* END OF jpnlButtonActions */

		/* jbtnShowAddForm - button for showing the add form dialog */
		JButton jbtnShowAddForm = new JButton("Add");
		jbtnShowAddForm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnShowAddForm.setBackground(Color.WHITE);
		jbtnShowAddForm.addActionListener(event -> {
			inspectionAddDialog.resetForm();
			inspectionAddDialog.setVisible(true);
		});
		jpnlButtonActions.add(jbtnShowAddForm);
		/* END OF jbtnShowAddForm */
		
		/* START OF jbtnUpdate */
		
		JButton jbtnUpdate = new JButton("Update");
		jbtnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnUpdate.setBackground(Color.WHITE);
		jbtnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblInspection.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel,
							"Please select an account first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				int databaseIdOfSelectedMember = (int) jtblInspection.getValueAt(selectedRowIndexOnTable, 0);

				inspectionUpdateDialog.initializeWithAccountId(databaseIdOfSelectedMember);

				inspectionUpdateDialog.setVisible(true);
			}
		});
		
		jbtnUpdate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jpnlButtonActions.add(jbtnUpdate);
		
		/* END OF jbtnUpdate */
		
		/* START OF jbtnDelete */
		
		JButton jbtnDelete = new JButton("Delete");
		jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnDelete.setBackground(Color.WHITE);
		jbtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblInspection.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select a member first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (JOptionPane.showConfirmDialog(thisPanel, 
						"Are you sure you want to delete this member?") == JOptionPane.YES_OPTION) {
					
					int databaseIdOfSelectedMember = (int) jtblInspection.getValueAt(selectedRowIndexOnTable, 0);
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
							"sims", "admin123");
							PreparedStatement deleteStatement = connection
									.prepareStatement("DELETE FROM inspection WHERE id = ?")){
						deleteStatement.setInt(1, databaseIdOfSelectedMember);
						deleteStatement.execute();
						
						JOptionPane.showMessageDialog(thisPanel, "Successfully deleted account.", "Success!",
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
		jbtnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jpnlButtonActions.add(jbtnDelete);
		
		/* END OF jbtnDelete */
		
		/* jscrlpnInspectionTable - Scrollable Table Panel */
		JScrollPane jscrlpnInspectionTable = new JScrollPane();
		add(jscrlpnInspectionTable);
		/* END OF jscrlpnInspectionTable */
		
		/* jtblInspection - Main Panel Table */
		jtblInspection = new JTable();
		jtblInspection.setRowHeight(25);
		jtblInspection.setIntercellSpacing(new Dimension(10, 10));
		jtblInspection.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		
		// Table Model
		inspectionTableModel = new InspectionTableModel();
		inspectionTableModel.inspectionManagementPanel = this;
		jtblInspection.setModel(inspectionTableModel);
		
		jscrlpnInspectionTable.setViewportView(jtblInspection);
		/* END OF jtblInspection */

		// Create the add form dialog
		inspectionAddDialog = new AddDialog();
		inspectionAddDialog.inspectionManagementPanel = this;
		
		// Create the update form dialog
		inspectionUpdateDialog = new UpdateDialog();
		inspectionUpdateDialog.inspectionManagementPanel = this;
	}
	
	/**
	 * Updates the table model and prompts for a JTable redraw.
	 */
	public void updateTable() {
		inspectionTableModel.update();
	}

}
