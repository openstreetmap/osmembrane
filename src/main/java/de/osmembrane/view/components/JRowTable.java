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

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Implements a variant of the {@link JTable} that does select the
 * {@link TableCellEditor}s based on rows instead of columns.
 * 
 * @author tobias_kuhn
 * 
 */
public class JRowTable extends JTable {

    private static final long serialVersionUID = -6750075292555886817L;

    /**
     * the row editor model
     */
    private RowEditorModel rowEditorModel = null;

    /**
     * @see JTable#JTable
     */
    public JRowTable() {
        super();
    }

    /**
     * @see JTable#JTable
     */
    public JRowTable(TableModel tm) {
        super(tm);
    }

    /**
     * @see JTable#JTable
     */
    public JRowTable(TableModel tm, TableColumnModel tcm) {
        super(tm, tcm);
    }

    /**
     * @see JTable#JTable
     */
    public JRowTable(TableModel tm, TableColumnModel tcm, ListSelectionModel lsm) {
        super(tm, tcm, lsm);
    }

    /**
     * @see JTable#JTable
     */
    public JRowTable(int rowCount, int colCount) {
        super(rowCount, colCount);
    }

    /**
     * @see JTable#JTable
     */
    public JRowTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    /**
     * @see JTable#JTable
     */
    public JRowTable(final Object[][] rowData, final Object[] columnNames) {
        super(rowData, columnNames);
    }

    /**
     * Initializes a new {@link JRowTable} with a row editor model
     * 
     * @param tm
     *            classic TableModel, see {@link JTable#JTable}
     * @param rem
     *            the RowEditorModel to work with
     */
    public JRowTable(TableModel tm, RowEditorModel rem) {
        super(tm, null, null);
        this.rowEditorModel = rem;
    }

    /**
     * @param rowEditorModel
     *            the rowEditorModel to set
     */
    public void setRowEditorModel(RowEditorModel rowEditorModel) {
        this.rowEditorModel = rowEditorModel;
    }

    /**
     * @return the rowEditorModel
     */
    public RowEditorModel getRowEditorModel() {
        return rowEditorModel;
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        if (rowEditorModel != null) {
            TableCellEditor tce = rowEditorModel.getEditorRow(row);
            if (tce != null) {
                return tce;
            }
        }

        return super.getCellEditor(row, column);
    }

}
