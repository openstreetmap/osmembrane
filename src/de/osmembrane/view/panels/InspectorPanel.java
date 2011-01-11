package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * The inspector panel component to realize the function inspector.
 * 
 * @author tobias_kuhn
 *
 */
public class InspectorPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -4331036066478472018L;
	
	/**
	 * the table that displays the data of the function
	 */
	protected JTable display;

	/**
	 * Initializes the inspector panel and display
	 */
	public InspectorPanel() {
		display = new JTable(new InspectorPanelTableModel());

		// ensure clicking results in an edit
		display.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = display.rowAtPoint(e.getPoint());
				display.editCellAt(row, 1);
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});

		display.setDefaultRenderer(String.class, new InspectorPanelTableRenderer());

		// make that the only component there
		setLayout(new GridLayout(1, 1));
		add(display);
	}

	@Override
	public void update(Observable o, Object arg) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The table model of the display table of the inspector panel.
	 * 
	 * @author tobias_kuhn
	 *
	 */
	class InspectorPanelTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 6495527693821933683L;
		
		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public int getRowCount() {
			return 2;
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return (column == 1);
		}
		
		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0: return "Key";
			default: return "Value";
			}
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {			
			return String.class;
		}
		
		@Override
		public Object getValueAt(int row, int column) {
			return "Bleh";
		}
		
		@Override
		public void setValueAt(Object aValue, int row, int column) {
			// TODO Auto-generated method stub
			//super.setValueAt(aValue, row, column);
		}
		
	}
	
	/**
	 * The custom cell renderer for the display table of the inspector panel.
	 * 
	 * @author tobias_kuhn
	 *
	 */
	class InspectorPanelTableRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = -8005963595998602494L;
		
		private final Color WHITE = Color.WHITE;
		private final Color LIGHT_YELLOW = new Color(1.0f, 1.0f, 0.7f);
		

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			
			// alternate colors white and yellow
			if (row % 2 == 0) {
				c.setBackground(WHITE);				
			} else {
				c.setBackground(LIGHT_YELLOW);
			}
			
			// suppress borders
			// does not work for edited cells
			if (c instanceof JComponent) {
				JComponent jc = (JComponent) c;
				jc.setBorder(null);
			}
			
			return c;
		}

	}

}