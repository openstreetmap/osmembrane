package de.osmembrane.view.components;

import java.util.HashMap;

import javax.swing.table.TableCellEditor;

/**
 * Implements a simple editor model for JTables that is based on rows instead of
 * columns.
 * 
 * @author tobias_kuhn
 * 
 */
public class RowEditorModel {

	private HashMap<Integer, TableCellEditor> rowEditors;

	/**
	 * Initializes a row editor model
	 */
	public RowEditorModel() {
		rowEditors = new HashMap<Integer, TableCellEditor>();
	}

	/**
	 * adds a TableCellEditor for a specific row
	 * @param row the row of the TableCellEditor
	 * @param tce the TableCellEditor to add
	 */
	public void addEditorRow(int row, TableCellEditor tce) {
		rowEditors.put(row, tce);
	}
	
	/**
	 * removes a TableCellEditor for a specific row
	 * @param row the row of the TableCellEditor
	 */
	public void removeEditorRow(int row) {
		rowEditors.remove(row);
	}

	/**
	 * gets a TableCellEditor for a specific row
	 * @param row the row of the TableCellEditor
	 * @return the requested TableCellEditor
	 */
	public TableCellEditor getEditorRow(int row) {
		return rowEditors.get(row);
	}

}
