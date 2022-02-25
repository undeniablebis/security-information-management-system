package co.adet.sims.ui.visitor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

/**
 * Visitor Management Panel of this Application. Contains a Table
 * showing all logged visits, and a Dialog shown by a button to
 * create new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is
 * shown by clicking the Attendance Button in the MainFrame's sidebar.
 * 
 * @author Bismillah C. Constantino
 *
 */
public class VisitorManagementPanel extends JPanel {

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
	protected AddDialog visitorAddDialog;
	
	protected VisitorTableModel visitorTableModel;
	
	/**
	 * Construct the panel.
	 */
	public VisitorManagementPanel() {
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
		JLabel jlblHeader = new JLabel("Manage Visitor");
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
		jbtnShowAddForm.setBackground(Color.WHITE);
		jbtnShowAddForm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnShowAddForm.addActionListener(event -> {
			visitorAddDialog.resetForm();
			visitorAddDialog.setVisible(true);
		});
		jpnlButtonActions.add(jbtnShowAddForm);
		/* END OF jbtnShowAddForm */
		
		/* jscrlpnVisitorTable - Scrollable Table Panel */
		JScrollPane jscrlpnVisitorTable = new JScrollPane();
		add(jscrlpnVisitorTable);
		/* END OF jscrlpnVisitorTable */
		
		/* jtblVisitorLog - Main Panel Table */
		jtblVisitorLog = new JTable();
		jtblVisitorLog.setRowHeight(25);
		jtblVisitorLog.setIntercellSpacing(new Dimension(10, 10));
		jtblVisitorLog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		/* END OF jscrlpnVisitorTable */
		
		/* jtblVisitorLog - Main Panel Table */
		jtblVisitorLog = new JTable();
		visitorTableModel = new VisitorTableModel();
		jtblVisitorLog.setModel(visitorTableModel);
		jtblVisitorLog.setRowHeight(25);
		jtblVisitorLog.setIntercellSpacing(new Dimension(10, 10));
		jtblVisitorLog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jscrlpnVisitorTable.setViewportView(jtblVisitorLog);
		
		// Create the add form dialog
		visitorAddDialog = new AddDialog();
		visitorAddDialog.visitorManagementPanel = this;
	}
	
	public void updateTable() {
		visitorTableModel.refresh();
	}

}
