package de.osmembrane.view.components;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class JTextFieldWithButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
	
	private JTextFieldWithButton jtfwb;
	
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
