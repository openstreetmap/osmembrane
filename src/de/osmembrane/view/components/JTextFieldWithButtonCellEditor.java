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
