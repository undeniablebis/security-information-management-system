package co.adet.sims.ui.inspection;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
 * Add form dialog for logging new inspections.
 * 
 * @author Rian Carlo Reyes
 *
 */
public class UpdateDialog extends JDialog {

	/**
	 * Default Serial Version UID (for serializability, not important, placed to
	 * remove warnings)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Building Inspection Management Panel that owns this dialog box.
	 */
	protected InspectionManagementPanel inspectionManagementPanel;

	// Form Components (Field Inputs)
	private JComboBox<String> jcmbBuildingName;
	private JTextField jtxtfldFloorNumber;
	private JTextField jtxtfldRoomNumbers;
	private JComboBox<String> jcmbGeneralCondition;
	private JComboBox<String> jcmbInspector;
	private JTextField jtxtfldDate;
	private JTextField jtxtfldTimeStarted;
	private JTextField jtxtfldTimeFinished;
	private JTextArea jtxtareaDescription;
	private JTextArea jtxtareaOtherNotes;
	private int accountIdCurrentlyBeingUpdated;


	/**
	 * Create the dialog.
	 */
	public UpdateDialog() {

		// For reference later
		UpdateDialog thisDialog = this;

		/* Dialog Properties */
		setMinimumSize(new Dimension(700, 600));
		setTitle("Add Attendance");
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
									"UPDATE inspection set building_name = ?, floor_number = ?, room_numbers = ?, general_condition = ?, security_guard_id = ?, date = ?, time_started = ?, time_finished = ?, description = ?, other_notes = ? where id = ?")) {

						updateStatement.setString(1, (String) jcmbBuildingName.getSelectedItem());
						updateStatement.setInt(2, Integer.parseInt(jtxtfldFloorNumber.getText()));
						updateStatement.setString(3, jtxtfldRoomNumbers.getText());
						updateStatement.setString(4, (String) jcmbGeneralCondition.getSelectedItem());
						updateStatement.setLong(5, Long.parseLong(((String) jcmbInspector.getSelectedItem()).split(" ")[0]));
						updateStatement.setString(6, LocalDate.parse(jtxtfldDate.getText()).toString());
						updateStatement.setString(7, LocalTime.parse(jtxtfldTimeStarted.getText()).toString());
						updateStatement.setString(8, LocalTime.parse(jtxtfldTimeFinished.getText()).toString());
						updateStatement.setString(9, jtxtareaDescription.getText());
						updateStatement.setString(10, jtxtareaOtherNotes.getText());
						updateStatement.setInt(11, accountIdCurrentlyBeingUpdated);
						
						updateStatement.execute();
						
					} catch (DateTimeParseException e) {
						// If an error occured while parsing the datetime fields,
						// output a friendly message
						JOptionPane.showMessageDialog(thisDialog,
								"Please check your date and time inputs. It must follow ISO Time.\n"
										+ "Date must be yyyy-MM-dd, time fields (work-in, work-out) must be HH:mm:ss",
								"Check your inputs!", JOptionPane.WARNING_MESSAGE);
						throw new RuntimeException(e);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(thisDialog,
								"An error occured while trying to save inspection.\n\nMessage: " + e);
						throw new RuntimeException(e);
					}

					return null;
				}

				@Override
				protected void done() {
					try {
						get();

						// After the inspection has been saved, show a friendly dialog box
						JOptionPane.showMessageDialog(thisDialog, "Successfully logged inspection.\n", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
						// Refresh the management panel table model
						inspectionManagementPanel.updateTable();

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
		gbl_jpnlForm.columnWeights = new double[] { 0.10, 0.40, 0.10, 1.0 };
		gbl_jpnlForm.rowWeights = new double[] { 0, 0, 0, 0, 0, 0, 0.05, 1.0, 0.40 };
		jpnlForm.setLayout(gbl_jpnlForm);
		/* END OF jpnlForm */

		/* jlblGeneralDetailsHeader - header to separate general details section */
		JLabel jlblGeneralDetailsHeader = new JLabel("General Details");
		jlblGeneralDetailsHeader.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
		GridBagConstraints gbc_jlblGeneralDetailsHeader = new GridBagConstraints();
		gbc_jlblGeneralDetailsHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblGeneralDetailsHeader.gridwidth = 4;
		gbc_jlblGeneralDetailsHeader.insets = new Insets(0, 0, 15, 0);
		gbc_jlblGeneralDetailsHeader.gridx = 0;
		gbc_jlblGeneralDetailsHeader.gridy = 0;
		jpnlForm.add(jlblGeneralDetailsHeader, gbc_jlblGeneralDetailsHeader);
		/* END OF jlblGeneralDetailsHeader */

		/* jlblBuildingName - label for building name input */
		JLabel jlblBuildingName = new JLabel("Building:");
		jlblBuildingName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblBuildingName = new GridBagConstraints();
		gbc_jlblBuildingName.insets = new Insets(0, 0, 5, 5);
		gbc_jlblBuildingName.anchor = GridBagConstraints.EAST;
		gbc_jlblBuildingName.gridx = 0;
		gbc_jlblBuildingName.gridy = 1;
		jpnlForm.add(jlblBuildingName, gbc_jlblBuildingName);
		/* END OF jlblBuildingName */

		/* jcmbBuildingName - combo box input for building name */
		jcmbBuildingName = new JComboBox<>();
		jcmbBuildingName.setModel(new DefaultComboBoxModel<>(new String[] { "MAIN_NORTH_WING", "MAIN_EAST_WING",
				"MAIN_SOUTH_WING", "MAIN_WEST_WING", "MAIN_CHK", "MAIN_ALUMNUS_BUILDING", "MAIN_OVAL_TAHANAN_NG_ALUMNI",
				"MAIN_OVAL_STAGE", "COLLEGE_OF_COMMUNICATION", "COLLEGE_OF_ENGINEERING_AND_ARCHITECTURE",
				"INSTITUTE_OF_TECHNOLOGY", "CONDOTEL", "HASMIN" }));
		jcmbBuildingName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jcmbBuildingName = new GridBagConstraints();
		gbc_jcmbBuildingName.insets = new Insets(0, 0, 5, 10);
		gbc_jcmbBuildingName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbBuildingName.gridx = 1;
		gbc_jcmbBuildingName.gridy = 1;
		jpnlForm.add(jcmbBuildingName, gbc_jcmbBuildingName);
		/* END OF jcmbBuildingName */

		/* jlblInspector - label for inspector input */
		JLabel jlblInspector = new JLabel("Inspector:");
		jlblInspector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblInspector = new GridBagConstraints();
		gbc_jlblInspector.anchor = GridBagConstraints.EAST;
		gbc_jlblInspector.insets = new Insets(0, 0, 5, 5);
		gbc_jlblInspector.gridx = 2;
		gbc_jlblInspector.gridy = 1;
		jpnlForm.add(jlblInspector, gbc_jlblInspector);
		/* END OF jlblInspector */

		/* jcmbInspector - combo box input for inspector (security guard) */
		jcmbInspector = new JComboBox<String>();
		jcmbInspector.setModel(new DefaultComboBoxModel<>(new String[] { "0001", "0002", "0003" }));
		jcmbInspector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jcmbInspector = new GridBagConstraints();
		gbc_jcmbInspector.insets = new Insets(0, 0, 5, 0);
		gbc_jcmbInspector.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbInspector.gridx = 3;
		gbc_jcmbInspector.gridy = 1;
		jpnlForm.add(jcmbInspector, gbc_jcmbInspector);
		/* END OF jcmbInspector */

		/* jlblFloorNumber - label for floor number input */
		JLabel jlblFloorNumber = new JLabel("Floor #:");
		jlblFloorNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblFloorNumber = new GridBagConstraints();
		gbc_jlblFloorNumber.anchor = GridBagConstraints.EAST;
		gbc_jlblFloorNumber.insets = new Insets(0, 0, 5, 5);
		gbc_jlblFloorNumber.gridx = 0;
		gbc_jlblFloorNumber.gridy = 2;
		jpnlForm.add(jlblFloorNumber, gbc_jlblFloorNumber);
		/* END OF jlblFloorNumber */

		/* jtxtfldFloorNumber - text field input for floor number */
		jtxtfldFloorNumber = new JTextField();
		jtxtfldFloorNumber.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldFloorNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldFloorNumber.setColumns(10);
		GridBagConstraints gbc_jtxtfldFloorNumber = new GridBagConstraints();
		gbc_jtxtfldFloorNumber.insets = new Insets(0, 0, 5, 10);
		gbc_jtxtfldFloorNumber.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldFloorNumber.gridx = 1;
		gbc_jtxtfldFloorNumber.gridy = 2;
		jpnlForm.add(jtxtfldFloorNumber, gbc_jtxtfldFloorNumber);
		/* END OF jtxtfldFloorNumber */

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

		/* jtxtfldDate - text fied input for date */
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

		/* jlblRoomNumbers - label for room numbers input */
		JLabel jlblRoomNumbers = new JLabel("Rooms:");
		jlblRoomNumbers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblRoomNumbers = new GridBagConstraints();
		gbc_jlblRoomNumbers.anchor = GridBagConstraints.EAST;
		gbc_jlblRoomNumbers.insets = new Insets(0, 0, 5, 5);
		gbc_jlblRoomNumbers.gridx = 0;
		gbc_jlblRoomNumbers.gridy = 3;
		jpnlForm.add(jlblRoomNumbers, gbc_jlblRoomNumbers);
		/* END OF jlblRoomNumbers */

		/* jtxtfldRoomNumbers - text field input for room numbers */
		jtxtfldRoomNumbers = new JTextField();
		jtxtfldRoomNumbers.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldRoomNumbers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldRoomNumbers.setColumns(10);
		GridBagConstraints gbc_jtxtfldRoomNumbers = new GridBagConstraints();
		gbc_jtxtfldRoomNumbers.insets = new Insets(0, 0, 5, 10);
		gbc_jtxtfldRoomNumbers.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldRoomNumbers.gridx = 1;
		gbc_jtxtfldRoomNumbers.gridy = 3;
		jpnlForm.add(jtxtfldRoomNumbers, gbc_jtxtfldRoomNumbers);
		/* END OF jtxtfldRoomNumbers */

		/* jlblTimeStarted - label for time started input */
		JLabel jlblTimeStarted = new JLabel("Started:");
		jlblTimeStarted.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblTimeStarted = new GridBagConstraints();
		gbc_jlblTimeStarted.anchor = GridBagConstraints.EAST;
		gbc_jlblTimeStarted.insets = new Insets(0, 0, 5, 5);
		gbc_jlblTimeStarted.gridx = 2;
		gbc_jlblTimeStarted.gridy = 3;
		jpnlForm.add(jlblTimeStarted, gbc_jlblTimeStarted);
		/* END OF jlblTimeStarted */

		/* jtxtfldTimeStarted - text field input for time started */
		jtxtfldTimeStarted = new JTextField();
		jtxtfldTimeStarted.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldTimeStarted.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldTimeStarted.setColumns(10);
		GridBagConstraints gbc_jtxtfldTimeStarted = new GridBagConstraints();
		gbc_jtxtfldTimeStarted.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldTimeStarted.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldTimeStarted.gridx = 3;
		gbc_jtxtfldTimeStarted.gridy = 3;
		jpnlForm.add(jtxtfldTimeStarted, gbc_jtxtfldTimeStarted);
		/* END OF jtxtfldTimeStarted */

		/* jlblGeneralCondition - label for general condition input */
		JLabel jlblGeneralCondition = new JLabel("Condition:");
		jlblGeneralCondition.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblGeneralCondition = new GridBagConstraints();
		gbc_jlblGeneralCondition.anchor = GridBagConstraints.EAST;
		gbc_jlblGeneralCondition.insets = new Insets(0, 0, 5, 5);
		gbc_jlblGeneralCondition.gridx = 0;
		gbc_jlblGeneralCondition.gridy = 4;
		jpnlForm.add(jlblGeneralCondition, gbc_jlblGeneralCondition);
		/* END OF jlblGeneralCondition */

		/* jcmbGeneralCondition - combo box input for general condition */
		jcmbGeneralCondition = new JComboBox<>();
		jcmbGeneralCondition.setModel(new DefaultComboBoxModel<>(new String[] { "Good", "Mediocre", "Bad" }));
		jcmbGeneralCondition.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jcmbGeneralCondition = new GridBagConstraints();
		gbc_jcmbGeneralCondition.insets = new Insets(0, 0, 5, 10);
		gbc_jcmbGeneralCondition.fill = GridBagConstraints.HORIZONTAL;
		gbc_jcmbGeneralCondition.gridx = 1;
		gbc_jcmbGeneralCondition.gridy = 4;
		jpnlForm.add(jcmbGeneralCondition, gbc_jcmbGeneralCondition);
		/* END OF jcmbGeneralCondition */

		/* jlblTimeFinished - label for time finished input */
		JLabel jlblTimeFinished = new JLabel("Finished:");
		jlblTimeFinished.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblTimeFinished = new GridBagConstraints();
		gbc_jlblTimeFinished.anchor = GridBagConstraints.EAST;
		gbc_jlblTimeFinished.insets = new Insets(0, 0, 5, 5);
		gbc_jlblTimeFinished.gridx = 2;
		gbc_jlblTimeFinished.gridy = 4;
		jpnlForm.add(jlblTimeFinished, gbc_jlblTimeFinished);
		/* END OF jlblTimeFinished */

		/* jtxtfldTimeFinished - text field input for time finished */
		jtxtfldTimeFinished = new JTextField();
		jtxtfldTimeFinished.setMargin(new Insets(4, 4, 4, 4));
		jtxtfldTimeFinished.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtfldTimeFinished.setColumns(10);
		GridBagConstraints gbc_jtxtfldTimeFinished = new GridBagConstraints();
		gbc_jtxtfldTimeFinished.insets = new Insets(0, 0, 5, 0);
		gbc_jtxtfldTimeFinished.fill = GridBagConstraints.HORIZONTAL;
		gbc_jtxtfldTimeFinished.gridx = 3;
		gbc_jtxtfldTimeFinished.gridy = 4;
		jpnlForm.add(jtxtfldTimeFinished, gbc_jtxtfldTimeFinished);
		/* END OF jtxtfldTimeFinished */

		/* jlblInspectionLogHeader - header to separate inspection log section */
		JLabel jlblInspectionLogHeader = new JLabel("Inspection Log");
		jlblInspectionLogHeader.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
		GridBagConstraints gbc_jlblInspectionLogHeader = new GridBagConstraints();
		gbc_jlblInspectionLogHeader.anchor = GridBagConstraints.WEST;
		gbc_jlblInspectionLogHeader.gridwidth = 4;
		gbc_jlblInspectionLogHeader.insets = new Insets(15, 0, 15, 0);
		gbc_jlblInspectionLogHeader.gridx = 0;
		gbc_jlblInspectionLogHeader.gridy = 5;
		jpnlForm.add(jlblInspectionLogHeader, gbc_jlblInspectionLogHeader);
		/* END OF jlblInspectionLogHeader */

		/* jlblDescription - label for description input */
		JLabel jlblDescription = new JLabel("Description:");
		jlblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblDescription = new GridBagConstraints();
		gbc_jlblDescription.anchor = GridBagConstraints.NORTHEAST;
		gbc_jlblDescription.insets = new Insets(0, 0, 5, 5);
		gbc_jlblDescription.gridx = 0;
		gbc_jlblDescription.gridy = 6;
		jpnlForm.add(jlblDescription, gbc_jlblDescription);
		/* END OF jlblDescription */

		/* jscrlpnDescription - scrollable container for description input */
		JScrollPane jscrlpnDescription = new JScrollPane();
		GridBagConstraints gbc_jscrlpnDescription = new GridBagConstraints();
		gbc_jscrlpnDescription.gridheight = 2;
		gbc_jscrlpnDescription.insets = new Insets(0, 0, 5, 10);
		gbc_jscrlpnDescription.fill = GridBagConstraints.BOTH;
		gbc_jscrlpnDescription.gridx = 1;
		gbc_jscrlpnDescription.gridy = 6;
		jpnlForm.add(jscrlpnDescription, gbc_jscrlpnDescription);
		/* END OF jtxtareaDescription */

		/* jtxtareaDescription - text area input for description */
		jtxtareaDescription = new JTextArea();
		jtxtareaDescription.setMargin(new Insets(4, 4, 4, 4));
		jtxtareaDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jscrlpnDescription.setViewportView(jtxtareaDescription);
		/* END OF jtxtareaDescription */

		
		/* jlblOtherNotes - label for other notes input */
		JLabel jlblOtherNotes = new JLabel("<html>Other<br>Notes:</html>");
		jlblOtherNotes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		GridBagConstraints gbc_jlblOtherNotes = new GridBagConstraints();
		gbc_jlblOtherNotes.anchor = GridBagConstraints.NORTHEAST;
		gbc_jlblOtherNotes.insets = new Insets(0, 0, 0, 5);
		gbc_jlblOtherNotes.gridx = 0;
		gbc_jlblOtherNotes.gridy = 8;
		jpnlForm.add(jlblOtherNotes, gbc_jlblOtherNotes);
		/* END OF jlblOtherNotes */

		/* jscrlpnOtherNotes - scrollable container for other notes input */
		JScrollPane jscrlpnOtherNotes = new JScrollPane();
		GridBagConstraints gbc_jscrlpnOtherNotes = new GridBagConstraints();
		gbc_jscrlpnOtherNotes.insets = new Insets(0, 0, 0, 10);
		gbc_jscrlpnOtherNotes.fill = GridBagConstraints.BOTH;
		gbc_jscrlpnOtherNotes.gridx = 1;
		gbc_jscrlpnOtherNotes.gridy = 8;
		jpnlForm.add(jscrlpnOtherNotes, gbc_jscrlpnOtherNotes);
		/* END OF jscrlpnOtherNotes */

		/* jtxtareaOtherNotes -text area input for other notes */
		jtxtareaOtherNotes = new JTextArea();
		jtxtareaOtherNotes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		jtxtareaOtherNotes.setMargin(new Insets(4, 4, 4, 4));
		jscrlpnOtherNotes.setViewportView(jtxtareaOtherNotes);
		/* END OF jtxtareaOtherNotes */


	}
	
public void initializeWithAccountId(int accountID) {
		
		try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sims_db",
				"sims", "admin123");
				PreparedStatement retrieveAccountByIdStatement = connection.prepareStatement("SELECT * FROM inspection WHERE id = ?")){
				
				retrieveAccountByIdStatement.setInt(1, accountID);
				retrieveAccountByIdStatement.execute();
				
				ResultSet accountResult = retrieveAccountByIdStatement.getResultSet();
				accountResult.next();
				
				accountIdCurrentlyBeingUpdated = accountResult.getInt("id");
				
				jcmbBuildingName.setSelectedItem(accountResult.getString("building_name"));
				jtxtfldFloorNumber.setText(accountResult.getString("floor_number"));
				jtxtfldRoomNumbers.setText(accountResult.getString("room_numbers"));
				jcmbGeneralCondition.setSelectedItem(accountResult.getString("general_condition"));
				jcmbInspector.setSelectedItem(accountResult.getString("security_guard_id"));
				jtxtfldDate.setText(accountResult.getString("date"));
				jtxtfldTimeStarted.setText(accountResult.getString("time_started"));
				jtxtfldTimeFinished.setText(accountResult.getString("time_finished"));
				jtxtareaDescription.setText(accountResult.getString("description"));
				jtxtareaOtherNotes.setText(accountResult.getString("other_notes"));
				
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "An error occured while trying to fetch account data. Please try again. \n\nDetails" 
					+ e.getMessage());
		}
	}

	/**
	 * Clears and resets the form.
	 */
	public void resetForm() {
		jcmbBuildingName.setSelectedIndex(0);
		jtxtfldFloorNumber.setText("");
		jtxtfldRoomNumbers.setText("");
		jcmbGeneralCondition.setSelectedIndex(0);
		jcmbInspector.setSelectedIndex(0);
		jtxtfldDate.setText("");
		jtxtfldTimeStarted.setText("");
		jtxtfldTimeFinished.setText("");
		jtxtareaDescription.setText("");
		jtxtareaOtherNotes.setText("");
		revalidate();
	}

}
