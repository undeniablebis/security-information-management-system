package co.adet.sims.ui.securityguards;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Add form dialog for adding security guards.
 * 
 * @author Elmer M. Cuenca
 *
 */
public class UpdateDialog extends JDialog {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to
	 * remove warnings)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Security Guard Management Panel that owns this dialog box.
	 */
	protected SecurityManagementPanel securityGuardManagementPanel;

	private final JPanel contentPanel = new JPanel();
	private JTextField jtxtfldFirstName;
	private JTextField jtxtfldMiddleName;
	private JTextField jtxtfldLastName;
	private JTextField jtxtfldSSSID;
	private JTextField jtxtfldTINNumber;
	private JRadioButton jrdbtnFemale;
	private JRadioButton jrdbtnMale;
	private int accountIdCurrentlyBeingUpdated;

	/**
	 * Create the dialog.
	 */
	public UpdateDialog() {

		// For reference later
		UpdateDialog thisDialog = this;

		// Title Details
		setTitle("New Employee Details");
		setResizable(false);

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			Component verticalStrut = Box.createVerticalStrut(20);
			GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
			gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
			gbc_verticalStrut.gridx = 0;
			gbc_verticalStrut.gridy = 1;
			contentPanel.add(verticalStrut, gbc_verticalStrut);
		}
		{
			JLabel jlblFirstName = new JLabel("First Name:");
			GridBagConstraints gbc_jlblFirstName = new GridBagConstraints();
			gbc_jlblFirstName.insets = new Insets(0, 0, 5, 5);
			gbc_jlblFirstName.anchor = GridBagConstraints.EAST;
			gbc_jlblFirstName.gridx = 0;
			gbc_jlblFirstName.gridy = 2;
			contentPanel.add(jlblFirstName, gbc_jlblFirstName);
		}
		{
			jtxtfldFirstName = new JTextField();
			GridBagConstraints gbc_jtxtfldFirstName = new GridBagConstraints();
			gbc_jtxtfldFirstName.anchor = GridBagConstraints.NORTH;
			gbc_jtxtfldFirstName.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldFirstName.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldFirstName.gridx = 1;
			gbc_jtxtfldFirstName.gridy = 2;
			contentPanel.add(jtxtfldFirstName, gbc_jtxtfldFirstName);
			jtxtfldFirstName.setColumns(10);
		}
		{
			JLabel jlblMiddleName = new JLabel("Middle Name:");
			GridBagConstraints gbc_jlblMiddleName = new GridBagConstraints();
			gbc_jlblMiddleName.anchor = GridBagConstraints.EAST;
			gbc_jlblMiddleName.insets = new Insets(0, 0, 5, 5);
			gbc_jlblMiddleName.gridx = 0;
			gbc_jlblMiddleName.gridy = 3;
			contentPanel.add(jlblMiddleName, gbc_jlblMiddleName);
		}
		{
			jtxtfldMiddleName = new JTextField();
			GridBagConstraints gbc_jtxtfldMiddleName = new GridBagConstraints();
			gbc_jtxtfldMiddleName.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldMiddleName.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldMiddleName.gridx = 1;
			gbc_jtxtfldMiddleName.gridy = 3;
			contentPanel.add(jtxtfldMiddleName, gbc_jtxtfldMiddleName);
			jtxtfldMiddleName.setColumns(10);
		}
		{
			JLabel jlblLastName = new JLabel("Last Name:");
			GridBagConstraints gbc_jlblLastName = new GridBagConstraints();
			gbc_jlblLastName.anchor = GridBagConstraints.EAST;
			gbc_jlblLastName.insets = new Insets(0, 0, 5, 5);
			gbc_jlblLastName.gridx = 0;
			gbc_jlblLastName.gridy = 4;
			contentPanel.add(jlblLastName, gbc_jlblLastName);
		}
		{
			jtxtfldLastName = new JTextField();
			GridBagConstraints gbc_jtxtfldLastName = new GridBagConstraints();
			gbc_jtxtfldLastName.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldLastName.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldLastName.gridx = 1;
			gbc_jtxtfldLastName.gridy = 4;
			contentPanel.add(jtxtfldLastName, gbc_jtxtfldLastName);
			jtxtfldLastName.setColumns(10);
		}
		{
			JLabel jlblSex = new JLabel("Sex:");
			GridBagConstraints gbc_jlblSex = new GridBagConstraints();
			gbc_jlblSex.anchor = GridBagConstraints.EAST;
			gbc_jlblSex.insets = new Insets(0, 0, 5, 5);
			gbc_jlblSex.gridx = 0;
			gbc_jlblSex.gridy = 5;
			contentPanel.add(jlblSex, gbc_jlblSex);
		}
		{

			JPanel jpnlSex = new JPanel();
			GridBagConstraints gbc_jpnlSex = new GridBagConstraints();
			gbc_jpnlSex.insets = new Insets(0, 0, 5, 0);
			gbc_jpnlSex.fill = GridBagConstraints.BOTH;
			gbc_jpnlSex.gridx = 1;
			gbc_jpnlSex.gridy = 5;
			contentPanel.add(jpnlSex, gbc_jpnlSex);
			ButtonGroup bttngrpSex = new ButtonGroup();
			{
				jrdbtnFemale = new JRadioButton("Female");
				jpnlSex.add(jrdbtnFemale);
				bttngrpSex.add(jrdbtnFemale);
			}
			{
				jrdbtnMale = new JRadioButton("Male");
				jpnlSex.add(jrdbtnMale);
				bttngrpSex.add(jrdbtnMale);
			}

		}
		{
			JLabel jlblSSSID = new JLabel("SSS ID:");
			GridBagConstraints gbc_jlblSSSID = new GridBagConstraints();
			gbc_jlblSSSID.anchor = GridBagConstraints.EAST;
			gbc_jlblSSSID.insets = new Insets(0, 0, 5, 5);
			gbc_jlblSSSID.gridx = 0;
			gbc_jlblSSSID.gridy = 6;
			contentPanel.add(jlblSSSID, gbc_jlblSSSID);
		}
		{
			jtxtfldSSSID = new JTextField();
			GridBagConstraints gbc_jtxtfldSSSID = new GridBagConstraints();
			gbc_jtxtfldSSSID.insets = new Insets(0, 0, 5, 0);
			gbc_jtxtfldSSSID.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldSSSID.gridx = 1;
			gbc_jtxtfldSSSID.gridy = 6;
			contentPanel.add(jtxtfldSSSID, gbc_jtxtfldSSSID);
			jtxtfldSSSID.setColumns(10);
		}
		{
			JLabel jlblTINNumber = new JLabel("TIN #:");
			GridBagConstraints gbc_jlblTINNumber = new GridBagConstraints();
			gbc_jlblTINNumber.anchor = GridBagConstraints.EAST;
			gbc_jlblTINNumber.insets = new Insets(0, 0, 0, 5);
			gbc_jlblTINNumber.gridx = 0;
			gbc_jlblTINNumber.gridy = 7;
			contentPanel.add(jlblTINNumber, gbc_jlblTINNumber);
		}
		{
			jtxtfldTINNumber = new JTextField();
			GridBagConstraints gbc_jtxtfldTINNumber = new GridBagConstraints();
			gbc_jtxtfldTINNumber.fill = GridBagConstraints.HORIZONTAL;
			gbc_jtxtfldTINNumber.gridx = 1;
			gbc_jtxtfldTINNumber.gridy = 7;
			contentPanel.add(jtxtfldTINNumber, gbc_jtxtfldTINNumber);
			jtxtfldTINNumber.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton jbtnSaveButton = new JButton("SAVE");
				jbtnSaveButton.addActionListener(event -> {
					
					List<String> errorMessageList = new ArrayList<>();
					
					String firstName = jtxtfldFirstName.getText();
					if(firstName.length() == 0 || firstName.length() > 45)
						errorMessageList.add("Invalid first name. Must not be empty and at most 45 characters.");
					
					String middleName = jtxtfldMiddleName.getText();
					if(middleName.length() == 0 || middleName.length() > 45)
						errorMessageList.add("Invalid middle name. Must not be empty and at most 45 characters.");
					
					String lastName = jtxtfldLastName.getText();
					if(lastName.length() == 0 || lastName.length() > 45)
						errorMessageList.add("Invalid last name. Must not be empty and at most 45 characters.");
					
					long sssNumber = 0;
					try {
						sssNumber = Long.parseLong(jtxtfldSSSID.getText());
					} catch(NumberFormatException e) {
						errorMessageList.add("Invalid sss number. Must be numeric.");
					}
					
					long tinNumber = 0;
					try {
						tinNumber = Long.parseLong(jtxtfldTINNumber.getText());
					} catch(NumberFormatException e) {
						errorMessageList.add("Invalid TIN number. Must be numeric.");
					}

					if (errorMessageList.size() > 0) {
						StringBuilder errorMessageBuilder = new StringBuilder();
						for (String errorMessage : errorMessageList) {
							errorMessageBuilder.append("\n- ");
							errorMessageBuilder.append(errorMessage);
						}
						JOptionPane.showMessageDialog(thisDialog,
								"Please correct the input errors below:" + errorMessageBuilder.toString());
						return;
						
					
					}
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
								"sims", "admin123");
						PreparedStatement updateStatement = connection.prepareStatement(
								"UPDATE security_guard set first_name = ?, middle_name = ?, last_name = ?, sex = ?, sss_id = ?, tin_number = ? where employee_id = ?")){
						/* Sex option */
						// String holder for user input for sex
						String sex = " ";

						if (jrdbtnFemale.isSelected())
							sex = "Female";
						else if (jrdbtnMale.isSelected())
							sex = "Male";
						
						updateStatement.setString(1, firstName);
						updateStatement.setString(2, middleName);
						updateStatement.setString(3, lastName);
						updateStatement.setString(4, sex);
						updateStatement.setLong(5, sssNumber);
						updateStatement.setLong(6, tinNumber);	
						updateStatement.setInt(7, accountIdCurrentlyBeingUpdated);
						updateStatement.execute();
						connection.close();

						JOptionPane.showMessageDialog(null, "Security guard successfully created!");

						// To close the dialog prompt right after creation
						this.resetForm();
						this.setVisible(false);

						// Update the table
						securityGuardManagementPanel.updateTable();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "An error occured while saving.\n\n Details:" + e);
					}
				});
				buttonPane.add(jbtnSaveButton);
				getRootPane().setDefaultButton(jbtnSaveButton);
			}
			{
				JButton jbtnCancelButton = new JButton("Cancel");
				jbtnCancelButton.addActionListener(event -> {
					thisDialog.setVisible(false);
				});
				buttonPane.add(jbtnCancelButton);
			}
		}
	}
	
	public void initializeWithAccountId(int accountID) {
		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
				"sims", "admin123");
				PreparedStatement retrieveAccountByIdStatement = connection.prepareStatement("SELECT * FROM security_guard WHERE employee_id = ?")){
				
				retrieveAccountByIdStatement.setInt(1, accountID);
				retrieveAccountByIdStatement.execute();
				
				ResultSet accountResult = retrieveAccountByIdStatement.getResultSet();
				accountResult.next();
				
				accountIdCurrentlyBeingUpdated = accountResult.getInt("employee_id");
				
				jtxtfldFirstName.setText(accountResult.getString("first_name"));
				jtxtfldMiddleName.setText(accountResult.getString("middle_name"));
				jtxtfldLastName.setText(accountResult.getString("last_name"));
				jtxtfldSSSID.setText(accountResult.getString("sss_id"));
				jtxtfldTINNumber.setText(accountResult.getString("tin_number"));
				
				String sex = accountResult.getString("sex");
				if(sex.equals("Male"))
					jrdbtnMale.setSelected(true);
				else if(sex.equals("Female"))
					jrdbtnFemale.setSelected(true);		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "An error occured while trying to fetch account data. Please try again. \n\nDetails" 
					+ e.getMessage());
		}
	}

	public void resetForm() {
		jtxtfldFirstName.setText("");
		jtxtfldMiddleName.setText("");
		jtxtfldLastName.setText("");
		jtxtfldSSSID.setText("");
		jtxtfldTINNumber.setText("");
	

	}
}
