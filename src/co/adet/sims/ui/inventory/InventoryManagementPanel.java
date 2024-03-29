package co.adet.sims.ui.inventory;

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
 * Inventory of Supplies of this Application. Contains a Table showing all
 * supplies logged in the system, and a add dialog form shown by a button to log
 * new entries.
 * 
 * An instance of this class is managed by MainFrame in itself, and is shown by
 * clicking the inventory Button in the MainFrame's sidebar.
 * 
 * @author Elmer M. Cuenca
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

	protected InventoryTableModel inventoryTableModel;

	/**
	 * Create the panel.
	 */
	public InventoryManagementPanel() {
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
		jbtnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jbtnAdd.addActionListener(event -> {
			inventoryAddDialog.resetForm();
			inventoryAddDialog.setVisible(true);
		});

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

	}

	public void updateTable() {
		inventoryTableModel.refresh();
	}

}
