package co.adet.sims.ui.car;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

/**
 * Car Management Panel of this Application. Contains a Table
 * showing all parked vehicles, and a Dialog shown by a button to
 * create new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is
 * shown by clicking the Attendance Button in the MainFrame's sidebar.
 * 
 * @author Bismillah C. Constantino
 *
 */
public class CarManagementPanel extends JPanel {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The main table of this panel.
	 */
	private JTable jtblCarLog;
	
	/**
	 * Add Form Dialog of this panel.
	 */
	protected AddDialog carAddDialog;
	protected UpdateDialog carUpdateDialog;
	
	protected CarTableModel carTableModel;
	
	/**
	 * Construct the panel.
	 */
	public CarManagementPanel() {
		
		CarManagementPanel thisPanel = this;
		
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
		JLabel jlblHeader = new JLabel("Manage Car");
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

		/* jbtnShowAddForm - button for adding new visitors */
		JButton jbtnShowAddForm = new JButton("Add");
		jbtnShowAddForm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnShowAddForm.addActionListener(event -> {
			carAddDialog.resetForm();
			carAddDialog.setVisible(true);
		});
		jpnlButtonActions.add(jbtnShowAddForm);
		/* END OF jbtnShowAddForm */
		
		/* jbtnShowUpdateForm - button for updating visitors' cars */
		JButton jbtnShowUpdateForm = new JButton("Update");
		jbtnShowUpdateForm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnShowUpdateForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndexOnTable = jtblCarLog.getSelectedRow();
				if(selectedRowIndexOnTable == -1) {
					JOptionPane.showMessageDialog(thisPanel, 
							"Please select a visitor first before clicking this button.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				String databaseIdOfSelectedParking = (String) jtblCarLog.getValueAt(selectedRowIndexOnTable, 0);
				
				carUpdateDialog.initializeWithSlotNumber(databaseIdOfSelectedParking);
				carUpdateDialog.setVisible(true);
				
				
			}
		});
		jpnlButtonActions.add(jbtnShowUpdateForm);
		
		
		/* jscrlpnCarTable - Scrollable Table Panel */
		JScrollPane jscrlpnCarTable = new JScrollPane();
		add(jscrlpnCarTable);
		/* END OF jscrlpnCarTable */
		
		/* jtblCarLog - Main Panel Table */
		jtblCarLog = new JTable();
		jtblCarLog.setRowHeight(25);
		jtblCarLog.setIntercellSpacing(new Dimension(10, 10));
		jtblCarLog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		/* END OF jscrlpnCarTable */
		
		/* jtblCarLog - Main Panel Table */
		jtblCarLog = new JTable();
		carTableModel = new CarTableModel();
		jtblCarLog.setModel(carTableModel);
		jtblCarLog.getColumnModel().getColumn(0).setPreferredWidth(100);
		jtblCarLog.getColumnModel().getColumn(1).setPreferredWidth(100);
		jtblCarLog.getColumnModel().getColumn(2).setPreferredWidth(200);
		jtblCarLog.getColumnModel().getColumn(3).setPreferredWidth(70);
		jtblCarLog.setRowHeight(25);
		jtblCarLog.setIntercellSpacing(new Dimension(10, 10));
		jtblCarLog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jscrlpnCarTable.setViewportView(jtblCarLog);
		/* END OF jtblCarLog */
		
		// Create the add form dialog
		carAddDialog = new AddDialog();
		carAddDialog.carManagementPanel = this;
		
		carUpdateDialog = new UpdateDialog();
		carUpdateDialog.carManagementPanel = this;
	}
	
	public void updateTable() {
		carTableModel.refresh();
	}

}
