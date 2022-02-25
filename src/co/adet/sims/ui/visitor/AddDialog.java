package co.adet.sims.ui.visitor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Add form dialog for adding visitors.
 * 
 * @author Bismillah C. Constantino
 *
 */
public class AddDialog extends JDialog {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to remove warnings)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Main Content Pane of this Frame
	 */
	private JPanel jpnlContentPane;
	
	//Form components
	private JTextField jtxtfldNameOfVisitor;
	private JLabel jlblPurposeOfVisit;
	private JLabel lblTimeOfVisit;
	private JScrollPane scrollPane;
	private JTextArea txtPurposeOfVisit;
	private JLabel lblTimeOfLeave;
	private JTextField jtxtfldTimeOfVisit;
	private JTextField jtxtfldTimeOfLeave;
	private JPanel buttonPane;
	private JButton jbtnOk;
	private JButton jbtnCancel;
	private JComboBox<String> jcmbVisitorType;

	protected VisitorManagementPanel visitorManagementPanel;

	
	public AddDialog() {
		//For reference later
		AddDialog thisDialog = this;
		
		//prevent user to resize the dialog
		setResizable(false);
		
		//set window size
		setMinimumSize(new Dimension(600, 400));
		
		//set title
		setTitle("Add Visitor");
		
		// Create the main content pane of this frame
		jpnlContentPane = new JPanel();
		jpnlContentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// Create the main content pane of this frame
		setContentPane(jpnlContentPane);
		
		// Use GridBagLayout for an eye-friendly form
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{150, 195, 127, 197};
		gridBagLayout.rowHeights = new int[] {60, 158, 30, 56, 0};
		gridBagLayout.columnWeights = new double[]{0.15, 0.85, 0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		jpnlContentPane.setLayout(gridBagLayout);
		
		/*  jlblNameOfVisitor  Name of Visitor Label      */
		JLabel jlblNameOfVisitor = new JLabel("Name of Visitor:");
		jlblNameOfVisitor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblNameOfVisitor = new GridBagConstraints();
		gbc_jlblNameOfVisitor.insets = new Insets(0, 0, 5, 5);
		gbc_jlblNameOfVisitor.gridx = 0;
		gbc_jlblNameOfVisitor.gridy = 0;
		jpnlContentPane.add(jlblNameOfVisitor, gbc_jlblNameOfVisitor);
		/*  END OF jlblNameOfVisitor          */
		
		/* jtxtfldNameOfVisitor */
		jtxtfldNameOfVisitor = new JTextField();
		jtxtfldNameOfVisitor.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldNameOfVisitor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jtxtfldNameOfVisitor = new GridBagConstraints();
		gbc_jtxtfldNameOfVisitor.insets = new Insets(0, 0, 5, 5);
		gbc_jtxtfldNameOfVisitor.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldNameOfVisitor.gridx = 1;
		gbc_jtxtfldNameOfVisitor.gridy = 0;
		jpnlContentPane.add(jtxtfldNameOfVisitor, gbc_jtxtfldNameOfVisitor);
		jtxtfldNameOfVisitor.setColumns(10);
		/* END OF jtxtfldNameOfVisitor  */
		
		/* jlbljlstTypeOfVisitor - Type of visitor */
		/* END OF jlbljlstTypeOfVisitor*/
		JLabel jlbljlstTypeOfVisitor = new JLabel("Type of visitor:");
		jlbljlstTypeOfVisitor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlbljlstTypeOfVisitor = new GridBagConstraints();
		gbc_jlbljlstTypeOfVisitor.insets = new Insets(0, 0, 5, 5);
		gbc_jlbljlstTypeOfVisitor.gridx = 2;
		gbc_jlbljlstTypeOfVisitor.gridy = 0;
		/* END OF comboBox */
		
		
		
		//Create combo box for visitor type
		//The use of combo box makes it appealing to the eyes of use
		jcmbVisitorType = new JComboBox<String>();
		jcmbVisitorType.setBackground(Color.WHITE);
		jcmbVisitorType.setModel(new DefaultComboBoxModel<>(new String[] {"Alumnus", "Parent"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 0;
		jpnlContentPane.add(jcmbVisitorType, gbc_comboBox);
		
		/* jlblPurposeOfVisit Purpose of Visit */
		jlblPurposeOfVisit = new JLabel("Purpose of visit:");
		jlblPurposeOfVisit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblPurposeOfVisit = new GridBagConstraints();
		gbc_jlblPurposeOfVisit.insets = new Insets(0, 0, 5, 5);
		gbc_jlblPurposeOfVisit.gridx = 0;
		gbc_jlblPurposeOfVisit.gridy = 1;
		jpnlContentPane.add(jlblPurposeOfVisit, gbc_jlblPurposeOfVisit);
		/* END OF jlblPurposeOfVisit*/
		
		//Added scrollpane for purpose of visit
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		jpnlContentPane.add(scrollPane, gbc_scrollPane);
		
		/* txtPurposeOfVisit Purpose of visit   */
		txtPurposeOfVisit = new JTextArea();
		txtPurposeOfVisit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		scrollPane.setViewportView(txtPurposeOfVisit);
		/*END OF txtPurposeOfVisit*/
		
		/* lblTimeOfVisit Time of visit*/
		lblTimeOfVisit = new JLabel("Time of visit:");
		lblTimeOfVisit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblTimeOfVisit = new GridBagConstraints();
		gbc_lblTimeOfVisit.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeOfVisit.gridx = 0;
		gbc_lblTimeOfVisit.gridy = 2;
		jpnlContentPane.add(lblTimeOfVisit, gbc_lblTimeOfVisit);
		/* END OF lblTimeOfVisit*/
		
		/* jtxtfldTimeOfVisit Time of Visit*/
		jtxtfldTimeOfVisit = new JTextField();
		jtxtfldTimeOfVisit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jtxtfldTimeOfVisit = new GridBagConstraints();
		gbc_jtxtfldTimeOfVisit.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldTimeOfVisit.insets = new Insets(0, 0, 5, 5);
		gbc_jtxtfldTimeOfVisit.gridx = 1;
		gbc_jtxtfldTimeOfVisit.gridy = 2;
		jpnlContentPane.add(jtxtfldTimeOfVisit, gbc_jtxtfldTimeOfVisit);
		jtxtfldTimeOfVisit.setColumns(10);
		/* END OF jtxtfldTimeOfVisit */
		
		/* END OF jtxtfldTimeOfLeave*/
		lblTimeOfLeave = new JLabel("Time of leave:");
		lblTimeOfLeave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_lblTimeOfLeave = new GridBagConstraints();
		gbc_lblTimeOfLeave.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeOfLeave.gridx = 2;
		gbc_lblTimeOfLeave.gridy = 2;
		jpnlContentPane.add(lblTimeOfLeave, gbc_lblTimeOfLeave);
		jtxtfldTimeOfLeave = new JTextField();
		jtxtfldTimeOfLeave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jtxtfldTimeOfLeave = new GridBagConstraints();
		gbc_jtxtfldTimeOfLeave.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldTimeOfLeave.anchor = GridBagConstraints.NORTH;
		gbc_jtxtfldTimeOfLeave.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldTimeOfLeave.gridx = 3;
		gbc_jtxtfldTimeOfLeave.gridy = 2;
		jpnlContentPane.add(jtxtfldTimeOfLeave, gbc_jtxtfldTimeOfLeave);
		jtxtfldTimeOfLeave.setColumns(10);
		
		//create panel that will hold the CANCEL and OK button
		buttonPane = new JPanel();
		buttonPane.setMaximumSize(new Dimension(32767, 40));
		buttonPane.setPreferredSize(new Dimension(10, 40));
		buttonPane.setMinimumSize(new Dimension(10, 40));
		GridBagConstraints gbc_buttonPane = new GridBagConstraints();
		gbc_buttonPane.fill = GridBagConstraints.BOTH;
		gbc_buttonPane.gridx = 3;
		gbc_buttonPane.gridy = 3;
		buttonPane.setFocusable(false);
		jpnlContentPane.add(buttonPane, gbc_buttonPane);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		//create and add buttons to the panel
		jbtnOk = new JButton("Ok");
		jbtnOk.setBackground(Color.WHITE);
		jbtnOk.setFocusable(false);
		jbtnOk.addActionListener(event ->{
			
			String nameOfVisitor = jtxtfldNameOfVisitor.getText();
			String purposeOfVisit = txtPurposeOfVisit.getText();
			String timeOfVisit = jtxtfldTimeOfVisit.getText();
			String timeOfLeave = jtxtfldTimeOfLeave.getText();
			
			
			try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db", 
					"sims", "admin123");
				PreparedStatement insertStatement = connection.prepareStatement(
						"INSERT INTO visitor_log(name_of_visitor, visitor_type, purpose_of_visit, time_of_visit, time_of_leave) VALUES(?, ?, ?, ?, ?)")){
				
				insertStatement.setString(1, nameOfVisitor);
				insertStatement.setString(2, (String) jcmbVisitorType.getSelectedItem());
				insertStatement.setString(3, purposeOfVisit);
				insertStatement.setString(4, timeOfVisit);
				insertStatement.setString(5, timeOfLeave);
				insertStatement.execute();
				connection.close();
				
				JOptionPane.showMessageDialog(thisDialog, "Visitor successfully added.");
				
				//
				this.resetForm();
				this.setVisible(false);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(thisDialog, "An error occured while saving.\n\nDetails: "+e);
			}
		});
		
		
		buttonPane.add(jbtnOk);
		getRootPane().setDefaultButton(jbtnOk);
		
		
		
		jbtnCancel = new JButton("Cancel");
		jbtnCancel.setBackground(Color.WHITE);
		jbtnCancel.setFocusable(false);
		jbtnCancel.addActionListener(event ->{
			this.setVisible(false);
		});
		buttonPane.add(jbtnCancel);
		
	}
	
	public void resetForm() {

		DateTimeFormatter noSeconds = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
				.withLocale(Locale.ENGLISH);
		
		
		jtxtfldNameOfVisitor.setText("");
		txtPurposeOfVisit.setText("");
		jtxtfldTimeOfVisit.setText(LocalTime.now().withNano(0).format(noSeconds).toString());
		jtxtfldTimeOfLeave.setText(" ");
		
		jtxtfldTimeOfVisit.setEditable(false);
		jtxtfldTimeOfLeave.setEditable(false);
	}
}
