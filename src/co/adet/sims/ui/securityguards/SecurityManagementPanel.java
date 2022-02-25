package co.adet.sims.ui.securityguards;

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




/**
 * Security Guard Management Panel. Contains a Table
 * showing all security guard, and  ADD Dialog
 * shown by a button to create new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is
 * shown by clicking the Attendance Button in the MainFrame's sidebar.
 * 
 * @author Elmer M. Cuenca
 *
 */

public class SecurityManagementPanel extends JPanel {
	
	/**
	 * Default Serial Version UID (for serializability, not important, placed to remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable jtblTable;
	
	/**
	 * Add form dialog for this panel
	 */
	protected AddDialog securityGuardAddDialog; 
	protected UpdateDialog securityGuardUpdateDialog;
	
	protected SecurityGuardTableModel securityGuardTableModel;
	

	/**
	 * Create the panel.
	 */
	public SecurityManagementPanel() {
		SecurityManagementPanel thisPanel = this;
		
		
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		/*
		 * Panel made for the buttons needed for Security Guards table 
		 */
		JPanel jpnlHeader = new JPanel();
		jpnlHeader.setBackground(Color.WHITE);
		jpnlHeader.setMinimumSize(new Dimension(10, 45));
		jpnlHeader.setAlignmentY(0.0f);
		jpnlHeader.setAlignmentX(0.0f);
		jpnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
		jpnlHeader.setMaximumSize(new Dimension(32767, 55));
		add(jpnlHeader);
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
		
		JLabel jlblHeader = new JLabel("Manage Security Guards");
		jlblHeader.setAlignmentX(0.0f);
		jlblHeader.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
		jpnlHeader.add(jlblHeader);
		
		JPanel jpnlButtons = new JPanel();
		jpnlButtons.setAlignmentX(0.0f);
		jpnlButtons.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) jpnlButtons.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		jpnlHeader.add(jpnlButtons);
		
		JButton jbtnAdd = new JButton("Add");
		jbtnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnAdd.setBackground(Color.WHITE);
		jbtnAdd.addActionListener(event -> {
			securityGuardAddDialog.resetForm();
			securityGuardAddDialog.setVisible(true);
		});
		jpnlButtons.add(jbtnAdd);
		
		JButton jbtnUpdate = new JButton("Update");
		jbtnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnUpdate.setBackground(Color.WHITE);
		jbtnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblTable.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel,
							"Please select an account first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				int databaseIdOfSelectedMember = (int) jtblTable.getValueAt(selectedRowIndexOnTable, 0);

				securityGuardUpdateDialog.initializeWithAccountId(databaseIdOfSelectedMember);

				securityGuardUpdateDialog.setVisible(true);
			}
		});
		
		jbtnUpdate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jpnlButtons.add(jbtnUpdate);
		
		JButton jbtnDelete = new JButton("Delete");
		jbtnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jbtnDelete.setBackground(Color.WHITE);
		jbtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblTable.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select a member first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (JOptionPane.showConfirmDialog(thisPanel, 
						"Are you sure you want to delete this member?") == JOptionPane.YES_OPTION) {
					
					int databaseIdOfSelectedMember = (int) jtblTable.getValueAt(selectedRowIndexOnTable, 0);
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
							"sims", "admin123");
							PreparedStatement deleteStatement = connection
									.prepareStatement("DELETE FROM security_guard WHERE employee_id = ?")){
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
		jpnlButtons.add(jbtnDelete);
		
		/*
		 * Panel made for the output of list of Security Guards in the database 
		 */
		JScrollPane jscrllpnTable = new JScrollPane();
		jscrllpnTable.setAlignmentY(0.0f);
		jscrllpnTable.setAlignmentX(0.0f);
		add(jscrllpnTable);
		
		jtblTable = new JTable();
		securityGuardTableModel = new SecurityGuardTableModel();
		jtblTable.setModel(securityGuardTableModel);
		jscrllpnTable.setViewportView(jtblTable);
		
		securityGuardAddDialog = new AddDialog();
		securityGuardUpdateDialog = new UpdateDialog();
		
		securityGuardUpdateDialog.securityGuardManagementPanel = this;
		securityGuardAddDialog.securityGuardManagementPanel = this;

	}
	
	public void updateTable() {
		securityGuardTableModel.refresh();
	}

}
