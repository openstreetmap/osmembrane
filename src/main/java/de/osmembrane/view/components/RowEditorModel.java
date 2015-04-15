/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.view.components;

import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Implements a simple editor model for {@link JTable}s that is based on rows
 * instead of columns.
 * 
 * @author tobias_kuhn
 * 
 */
public class RowEditorModel {

    private HashMap<Integer, TableCellEditor> rowEditors;

    /**
     * Initializes a new {@link RowEditorModel}
     */
    public RowEditorModel() {
        rowEditors = new HashMap<Integer, TableCellEditor>();
    }

    /**
     * sets a {@link TableCellEditor} for a specific row
     * 
     * @param row
     *            the row of the TableCellEditor
     * @param tce
     *            the TableCellEditor to set
     */
    public void setEditorRow(int row, TableCellEditor tce) {
        rowEditors.put(row, tce);
    }

    /**
     * removes a {@link TableCellEditor} for a specific row
     * 
     * @param row
     *            the row of the TableCellEditor
     */
    public void removeEditorRow(int row) {
        rowEditors.remove(row);
    }

    /**
     * gets a {@link TableCellEditor} for a specific row
     * 
     * @param row
     *            the row of the TableCellEditor
     * @return the requested TableCellEditor
     */
    public TableCellEditor getEditorRow(int row) {
        return rowEditors.get(row);
    }

    /**
     * clears the model
     */
    public void clear() {
        rowEditors.clear();
    }

}
