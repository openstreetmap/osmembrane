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

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * A cell editor for {@link JTextFieldWithButton}. How and why it works is a
 * mystery to me, but who cares.
 * 
 * @author tobias_kuhn
 * 
 */
public class JTextFieldWithButtonCellEditor extends AbstractCellEditor
        implements TableCellEditor {

    private static final long serialVersionUID = 5432943873776422277L;

    /**
     * {@link JTextFieldWithButton} to be stored and edited
     */
    private JTextFieldWithButton jtfwb;

    /**
     * Creates a new {@link JTextFieldWithButtonCellEditor} for jtfwb
     * 
     * @param jtfwb
     */
    public JTextFieldWithButtonCellEditor(JTextFieldWithButton jtfwb) {
        this.jtfwb = jtfwb;
    }

    @Override
    public Object getCellEditorValue() {
        return jtfwb.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return jtfwb;
    }

}
