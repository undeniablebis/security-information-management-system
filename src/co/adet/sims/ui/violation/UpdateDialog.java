package co.adet.sims.ui.violation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutionException;

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
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

/**
 * Update form dialog for Violations.
 * 
 * @author Jeremy A. Cube
 */
public class UpdateDialog extends JDialog {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to
	 * remove warnings)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Violator Management Panel that owns this dialog box.
	 */
	protected ViolationManagementPanel violationManagementPanel;

	// Form Components (Field Inputs)
	private JTextField jtxtfldCompleteName;
	private JTextField jtxtfldContactNumber;
	private JTextField jtxtfldViolationType;
	private JTextField jtxtfldDate;
	private JTextField jtxtfldTime;
	private JComboBox<String> jcmbStatus;
	private int accountIdCurrentlyBeingUpdated;

	/**
	 * Create the dialog.
	 */
	public UpdateDialog() {

		// For reference later
		UpdateDialog thisDialog = this;

		/* Dialog Properties */
		setMinimumSize(new Dimension(700, 600));
		setTitle("Log new Violation");
		/* END OF Dialog Properties */

		/* jpnlButtonActions - button actions panel */
		JPanel jpnlButtonActions = new JPanel();
		jpnlButtonActions.setBorder(new EmptyBorder(0, 0, 10, 15));
		FlowLayout flowLayout = (FlowLayout) jpnlButtonActions.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(jpnlButtonActions, BorderLayout.SOUTH);
		/* END OF jpnlButtonActions */

		/* jbtnCancel - cancel button to hide dialog */
		JButton jbtnCancel = new JButton("Cancel");
		jbtnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jpnlButtonActions.add(jbtnCancel);
		/* END OF jbtnCancel */

		/* jbtnLog - save button */
		JButton jbtnLog = new JButton("Log");
		jbtnLog.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		// Attach Action Listener (Click)
		jbtnLog.addActionListener(event -> {
			// Save the constructed attendance object, with a SwingWorker
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
							"sims", "admin123");
							PreparedStatement updateStatement = connection.prepareStatement(
									"UPDATE violation set violator_name = ?, violator_contact_number = ?, type = ?, committed_on = ?, status = ? where id = ?")){

						updateStatement.setString(1, jtxtfldCompleteName.getText());
						updateStatement.setString(2, jtxtfldContactNumber.getText());
						updateStatement.setString(3, jtxtfldViolationType.getText());
						updateStatement.setString(4, LocalDateTime.parse(jtxtfldDate.getText() + "T" + jtxtfldTime.getText()).toString());
						updateStatement.setString(5, (String) jcmbStatus.getSelectedItem());
						updateStatement.setInt(6, accountIdCurrentlyBeingUpdated);
						updateStatement.execute();
						connection.close();
						
						violationManagementPanel.updateTable();
						
					}
					catch (DateTimeParseException e) {
						// If an error occured while parsing the datetime fields,
						// output a friendly message
						JOptionPane.showMessageDialog(thisDialog,
								"Please check your date and time inputs. It must follow ISO Time.\n"
										+ "Date must be yyyy-MM-dd, time fields (work-in, work-out) must be HH:mm:ss",
								"Check your inputs!", JOptionPane.WARNING_MESSAGE);
						throw new RuntimeException(e);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(thisDialog,
								"An error occured while trying to save attendance.\n\nMessage: " + e);
						throw new RuntimeException(e);
					}

					return null;
				}

				@Override
				protected void done() {
					try {
						get();

						// After the violation has been saved, show a friendly dialog box
						JOptionPane.showMessageDialog(thisDialog, "Successfully logged violation.\n", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
						// Refresh the management panel table model
						violationManagementPanel.updateTable();

						// Hide this add dialog
						thisDialog.setVisible(false);
					} catch (InterruptedException | ExecutionException e) {
						JOptionPane.showMessageDialog(thisDialog, "An error occured while saving.\n\nMessage:" + e);
					}
				}
			}.execute();
		});
		jpnlButtonActions.add(jbtnLog);
		/* END OF jbtnLog */

		/* jpnlForm - main form panel, uses GridBagLayout */
		JPanel jpnlForm = new JPanel();
		jpnlForm.setBorder(new EmptyBorder(20, 20, 0, 20));
		getContentPane().add(jpnlForm, BorderLayout.CENTER);
		GridBagLayout gbl_jpnlForm = new GridBagLayout();
		gbl_jpnlForm.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_jpnlForm.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_jpnlForm.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_jpnlForm.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		jpnlForm.setLayout(gbl_jpnlForm);
		/* END OF jpnlForm */

		/* jlblQuickDetailsHeader - header to separate quick details section */
		JLabel jlblQuickDetailsHeader = new JLabel("Quick Details");
		jlblQuickDetailsHeader.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
		GridBagConstraints gbc_jlblQuickDetailsHeader = new GridBagConstraints();
		gbc_jlblQuickDetailsHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblQuickDetailsHeader.gridwidth = 4;
		gbc_jlblQuickDetailsHeader.insets = new Insets(0, 0, 15, 0);
		gbc_jlblQuickDetailsHeader.gridx = 0;
		gbc_jlblQuickDetailsHeader.gridy = 0;
		jpnlForm.add(jlblQuickDetailsHeader, gbc_jlblQuickDetailsHeader);
		/* END OF jlblQuickDetailsHeader */

		/* jlblCompleteName - label for complete name input */
		JLabel jlblCompleteName = new JLabel("Complete Name:");
		jlblCompleteName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblCompleteName = new GridBagConstraints();
		gbc_jlblCompleteName.insets = new Insets(0, 0, 5, 5);
		gbc_jlblCompleteName.anchor = GridBagConstraints.EAST;
		gbc_jlblCompleteName.gridx = 0;
		gbc_jlblCompleteName.gridy = 1;
		jpnlForm.add(jlblCompleteName, gbc_jlblCompleteName);
		/* END OF jlblCompleteName */

		/* jtxtfldCompleteName - text field input for complete name */
		jtxtfldCompleteName = new JTextField();
		jtxtfldCompleteName.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldCompleteName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jtxtfldCompleteName = new GridBagConstraints();
		gbc_jtxtfldCompleteName.insets = new Insets(0, 0, 5, 10);
		gbc_jtxtfldCompleteName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldCompleteName.gridx = 1;
		gbc_jtxtfldCompleteName.gridy = 1;
		jpnlForm.add(jtxtfldCompleteName, gbc_jtxtfldCompleteName);
		jtxtfldCompleteName.setColumns(10);
		/* END OF jtxtfldCompleteName */

		/* jlblViolationType - label for violation type input */
		JLabel jlblViolationType = new JLabel("Violation Type:");
		jlblViolationType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblViolationType = new GridBagConstraints();
		gbc_jlblViolationType.anchor = GridBagConstraints.EAST;
		gbc_jlblViolationType.insets = new Insets(0, 0, 5, 5);
		gbc_jlblViolationType.gridx = 2;
		gbc_jlblViolationType.gridy = 1;
		jpnlForm.add(jlblViolationType, gbc_jlblViolationType);
		/* END OF jlblViolationType */

		/* jtxtfldViolationType - text field input for violation type */
		jtxtfldViolationType = new JTextField();
		jtxtfldViolationType.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldViolationType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldViolationType.setColumns(10);
		GridBagConstraints gbc_jtxtfldViolationType = new GridBagConstraints();
		gbc_jtxtfldViolationType.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldViolationType.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldViolationType.gridx = 3;
		gbc_jtxtfldViolationType.gridy = 1;
		jpnlForm.add(jtxtfldViolationType, gbc_jtxtfldViolationType);
		/* END OF jtxtfldViolationType */


		/* jlblDate - label for date input */
		JLabel jlblDate = new JLabel("Date:");
		jlblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblDate = new GridBagConstraints();
		gbc_jlblDate.anchor = GridBagConstraints.EAST;
		gbc_jlblDate.insets = new Insets(0, 0, 5, 5);
		gbc_jlblDate.gridx = 2;
		gbc_jlblDate.gridy = 2;
		jpnlForm.add(jlblDate, gbc_jlblDate);
		/* END OF jlblDate */

		/* jtxtfldDate - text field input for date */
		jtxtfldDate = new JTextField();
		jtxtfldDate.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldDate.setColumns(10);
		GridBagConstraints gbc_jtxtfldDate = new GridBagConstraints();
		gbc_jtxtfldDate.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldDate.gridx = 3;
		gbc_jtxtfldDate.gridy = 2;
		jpnlForm.add(jtxtfldDate, gbc_jtxtfldDate);
		/* END OF jtxtfldDate */

		/* jlblContactNumber - label for contact number input */
		JLabel jlblContactNumber = new JLabel("Contact #:");
		jlblContactNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblContactNumber = new GridBagConstraints();
		gbc_jlblContactNumber.anchor = GridBagConstraints.EAST;
		gbc_jlblContactNumber.insets = new Insets(0, 0, 5, 5);
		gbc_jlblContactNumber.gridx = 0;
		gbc_jlblContactNumber.gridy = 3;
		jpnlForm.add(jlblContactNumber, gbc_jlblContactNumber);
		/* END OF jlblContactNumber */

		/* jtxtfldContactNumber - text field input for contact number */
		jtxtfldContactNumber = new JTextField();
		jtxtfldContactNumber.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldContactNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldContactNumber.setColumns(10);
		GridBagConstraints gbc_jtxtfldContactNumber = new GridBagConstraints();
		gbc_jtxtfldContactNumber.insets = new Insets(0, 0, 5, 10);
		gbc_jtxtfldContactNumber.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldContactNumber.gridx = 1;
		gbc_jtxtfldContactNumber.gridy = 3;
		jpnlForm.add(jtxtfldContactNumber, gbc_jtxtfldContactNumber);
		/* END OF jtxtfldContactNumber */

		/* jlblTime - label for time input */
		JLabel jlblTime = new JLabel("Time:");
		jlblTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblTime = new GridBagConstraints();
		gbc_jlblTime.anchor = GridBagConstraints.EAST;
		gbc_jlblTime.insets = new Insets(0, 0, 5, 5);
		gbc_jlblTime.gridx = 2;
		gbc_jlblTime.gridy = 3;
		jpnlForm.add(jlblTime, gbc_jlblTime);
		/* END OF jlblTime */

		/* jtxtfldTime - text field input for time */
		jtxtfldTime = new JTextField();
		jtxtfldTime.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldTime.setColumns(10);
		GridBagConstraints gbc_jtxtfldTime = new GridBagConstraints();
		gbc_jtxtfldTime.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldTime.gridx = 3;
		gbc_jtxtfldTime.gridy = 3;
		jpnlForm.add(jtxtfldTime, gbc_jtxtfldTime);
		/* END OF jtxtfldTime */


		/* jlblStatus - label for status input */
		JLabel jlblStatus = new JLabel("Status:");
		jlblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblStatus = new GridBagConstraints();
		gbc_jlblStatus.anchor = GridBagConstraints.EAST;
		gbc_jlblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_jlblStatus.gridx = 2;
		gbc_jlblStatus.gridy = 4;
		jpnlForm.add(jlblStatus, gbc_jlblStatus);
		/* END OF jlblStatus */

		/* jcmbStatus - combo box input for status */
		jcmbStatus = new JComboBox<>();
		jcmbStatus.setModel(new DefaultComboBoxModel<>(new String[] { "Recent", "Viewed", "Discarded" }));
		jcmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jcmbStatus = new GridBagConstraints();
		gbc_jcmbStatus.insets = new Insets(0, 0, 5, 0);
		gbc_jcmbStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbStatus.gridx = 3;
		gbc_jcmbStatus.gridy = 4;
		jpnlForm.add(jcmbStatus, gbc_jcmbStatus);
		/* END OF jcmbStatus */

	}
	
public void initializeWithAccountId(int accountID) {
		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
				"sims", "admin123");
				PreparedStatement retrieveAccountByIdStatement = connection.prepareStatement("SELECT * FROM violation WHERE id = ?")){
				
				retrieveAccountByIdStatement.setInt(1, accountID);
				retrieveAccountByIdStatement.execute();
				
				ResultSet accountResult = retrieveAccountByIdStatement.getResultSet();
				accountResult.next();
				
				accountIdCurrentlyBeingUpdated = accountResult.getInt("id");
				
				jtxtfldCompleteName.setText(accountResult.getString("violator_name"));
				jtxtfldViolationType.setText(accountResult.getString("type"));
				jtxtfldDate.setText(accountResult.getString("viodate"));
				jtxtfldTime.setText(accountResult.getString("viotime"));
				jtxtfldContactNumber.setText(accountResult.getString("violator_contact_number"));
				jcmbStatus.setSelectedItem(accountResult.getString("status"));
				
				
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "An error occured while trying to fetch account data. Please try again. \n\nDetails" 
					+ e.getMessage());
		}
	}

	/**
	 * Clears and resets the form.
	 */
	public void resetForm() {
		jtxtfldCompleteName.setText("");
		jtxtfldContactNumber.setText("");
		jtxtfldViolationType.setText("");
		jtxtfldDate.setText("");
		jtxtfldTime.setText("");
		jcmbStatus.setSelectedIndex(0);
	}

}
