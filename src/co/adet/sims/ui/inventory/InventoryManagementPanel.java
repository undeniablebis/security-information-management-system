package co.adet.sims.ui.inventory;

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

import co.adet.sims.ui.inventory.InventoryManagementPanel;


/**
 * Inventory of Supplies of this Application. Contains a Table showing all
 * supplies logged in the system, and a add dialog form shown by a button to log
 * new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is shown by
 * clicking the inventory Button in the MainFrame's sidebar.
 * 
 * @author Jeremy A. Cube
 *
 */

public class InventoryManagementPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable jtblTable;

	/**
	 * Add form dialog of this panel
	 */
	protected AddSuppliesDialog inventoryAddDialog;
	protected UpdateDialog inventoryUpdateDialog;

	protected InventoryTableModel inventoryTableModel;

	/**
	 * Create the panel.
	 */
	public InventoryManagementPanel() {
		InventoryManagementPanel thisPanel = this;
		
		
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel jpnlHeader = new JPanel();
		jpnlHeader.setBackground(Color.WHITE);
		jpnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
		jpnlHeader.setMaximumSize(new Dimension(32767, 50));
		add(jpnlHeader);
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));

		/* jlblHeader - Main Header Label */
		jpnlHeader.setLayout(new BoxLayout(jpnlHeader, BoxLayout.X_AXIS));
		JLabel jlblHeader = new JLabel("Manage Supplies");
		jlblHeader.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
		jpnlHeader.add(jlblHeader);
		/* END OF jlblHeader */

		JPanel jpnlButtonActions = new JPanel();
		jpnlButtonActions.setBackground(Color.WHITE);
		FlowLayout fl_jpnlButtonActions = (FlowLayout) jpnlButtonActions.getLayout();
		fl_jpnlButtonActions.setAlignment(FlowLayout.RIGHT);
		jpnlHeader.add(jpnlButtonActions);

		JButton jbtnAdd = new JButton("Add");
		jpnlButtonActions.add(jbtnAdd);
		jbtnAdd.setBackground(Color.WHITE);
		jbtnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnAdd.addActionListener(event -> {
			inventoryAddDialog.resetForm();
			inventoryAddDialog.setVisible(true);
		});
		
		
		/* START OF jbtnUpdate */
		
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
				long databaseIdOfSelectedMember = (long) jtblTable.getValueAt(selectedRowIndexOnTable, 0);

				inventoryUpdateDialog.initializeWithAccountId(databaseIdOfSelectedMember);

				inventoryUpdateDialog.setVisible(true);
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
				int selectedRowIndexOnTable = jtblTable.getSelectedRow();
				if (selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select a member first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (JOptionPane.showConfirmDialog(thisPanel, 
						"Are you sure you want to delete this member?") == JOptionPane.YES_OPTION) {
					
					long databaseIdOfSelectedMember = (long) jtblTable.getValueAt(selectedRowIndexOnTable, 0);
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
							"sims", "admin123");
							PreparedStatement deleteStatement = connection
									.prepareStatement("DELETE FROM inventory_of_supplies WHERE product_code = ?")){
						deleteStatement.setLong(1, databaseIdOfSelectedMember);
						deleteStatement.execute();
						
						JOptionPane.showMessageDialog(thisPanel, "Successfully deleted row.", "Success!",
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
		

		JScrollPane jscrllpnTable = new JScrollPane();
		add(jscrllpnTable);

		jtblTable = new JTable();
		inventoryTableModel = new InventoryTableModel();
		jtblTable.setModel(inventoryTableModel);
		jtblTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		jtblTable.getColumnModel().getColumn(2).setPreferredWidth(90);
		jtblTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		jscrllpnTable.setViewportView(jtblTable);

		// Create the add form dialog
		inventoryAddDialog = new AddSuppliesDialog();
		inventoryAddDialog.inventoryManagementPanel = this;
		
		// Create the update form dialog
		inventoryUpdateDialog = new UpdateDialog();
		inventoryUpdateDialog.inventoryManagementPanel = this;
	}

	public void updateTable() {
		inventoryTableModel.refresh();
	}

}
