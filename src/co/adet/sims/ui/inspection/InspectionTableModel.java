package co.adet.sims.ui.inspection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

public class InspectionTableModel extends AbstractTableModel {

	private class InspectionRecord {
		String date;
		String buildingFloor;
		String roomNumbers;
		int numberOfIssues;
		String condition;
	}

	/**
	 * Default Serial Version UID (for serializability, not important, placed to
	 * remove warnings)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Column names for this model.
	 */
	private static final String[] columnNames = { "#", "Date", "Building-Floor", "Rooms", "# of Issues", "Condition" };

	/**
	 * Inspection Management Panel that owns this dialog box.
	 */
	protected InspectionManagementPanel inspectionManagementPanel;

	/**
	 * Internal cache of this model.<br>
	 * <br>
	 * 
	 * A call to update() will update this cache, and prompts redraw of the
	 * listening JTable.
	 */
	private List<InspectionRecord> internalCache;

	public InspectionTableModel() {
		super();
		internalCache = new ArrayList<>();
	}

	/**
	 * Returns the column count of this Model.
	 * 
	 * @return the column count
	 */
	@Override
	public int getColumnCount() {
		/*
		 * Since the number of columns of this table is the same as the number of
		 * elements in the columnNames array, just return its length field.
		 */
		return columnNames.length;
	}

	/**
	 * Returns the column name on the specified columnIndex.
	 * 
	 * @param columnIndex the index of the column
	 * @return the column name
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	/**
	 * Gets the row count of this TableModel. It depends on how many elements there
	 * are in the internal cache.
	 * 
	 * @return the row count
	 */
	@Override
	public int getRowCount() {
		return internalCache.size();
	}

	/**
	 * Gets the value at the specified row and column. Since it has a backing
	 * repository as its source of data of inspections, a cache is implemented that
	 * gets updated only for certain events.
	 * 
	 * @param rowIndex    the row index of the value
	 * @param columnIndex the column index of the value
	 * @return the value
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// Since each row in the Table refers to each element in the cache,
		// and apparently the indexing scheme of JTable is zero-based
		// (which is the same as the definition of the List ADT),
		// we can easily retrieve the inspection at the specified rowIndex.
		InspectionRecord inspectionRecord = internalCache.get(rowIndex);

		// Depending on the column specified,
		// return the appropriate field of this attendance record.
		switch (columnIndex) {

		// First Column - Row Number (1-based for user-friendliness)
		case 0:
			return rowIndex + 1;

		// Second Column - Date
		case 1:
			return inspectionRecord.date;

		// Third Column - Building - Floor
		case 2:
			return inspectionRecord.buildingFloor;

		// Fourth Column - Room Numbers
		case 3:
			return inspectionRecord.roomNumbers;

		// Fifth Column - Issue Count
		case 4:
			return inspectionRecord.numberOfIssues;

		// Sixth Column - General Condition
		case 5:
			return inspectionRecord.condition;

		default:
			return null;

		}
	}

	/**
	 * Refreshes the TableModel with new data, and prompts redraw of the listening
	 * JTable.
	 */
	public void update() {
		// Construct a SwingWorker to fetch data from the repository,
		// and execute it.
		new SwingWorker<List<InspectionRecord>, Void>() {
			@Override
			protected List<InspectionRecord> doInBackground() throws Exception {
				try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pupsims_db",
						"pupsims", "pupsimspass_123");
						Statement retrieveStatement = connection.createStatement();
						ResultSet inspectionsResultSet = retrieveStatement.executeQuery(
								"SELECT inspection.*, COUNT(inspection_issue.description) FROM inspection LEFT JOIN inspection_issue ON inspection_issue.inspection_id = inspection.id GROUP BY inspection.id")) {

					List<InspectionRecord> inspections = new ArrayList<>();
					while (inspectionsResultSet.next()) {
						InspectionRecord inspectionRecord = new InspectionRecord();
						inspectionRecord.date = inspectionsResultSet.getString("date");
						inspectionRecord.buildingFloor = inspectionsResultSet.getString("building_name") + " Floor: "
								+ inspectionsResultSet.getString("floor_number");
						inspectionRecord.roomNumbers = inspectionsResultSet.getString("room_numbers");
						inspectionRecord.numberOfIssues = inspectionsResultSet.getInt("COUNT(inspection_issue.description)");
						inspectionRecord.condition = inspectionsResultSet.getString("general_condition");
						inspections.add(inspectionRecord);
					}

					return inspections;
				}
			}

			@Override
			protected void done() {
				try {
					// Update the cache of this TableModel
					internalCache = get();
					// Notify JTable that data in this Model has changed
					fireTableDataChanged();
				} catch (InterruptedException e) {
					// TODO: Sophisticated handling of InterruptedException
					// maybe a message dialog.
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO: Sophisticated handling of InterruptedException
					// maybe a message dialog.
					e.printStackTrace();
				}
			}
		}.execute();
	}

}
