package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.EditPropertyAction;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.xml.XMLHasDescription;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.components.JRowTable;
import de.osmembrane.view.components.RowEditorModel;

/**
 * The inspector panel component to realize the function inspector.
 * 
 * @author tobias_kuhn
 * 
 */
public class InspectorPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -4331036066478472018L;

	/**
	 * the label that shows the function name
	 */
	private JLabel caption;

	/**
	 * the table that displays the data of the function
	 */
	private JRowTable display;
	private RowEditorModel rowEditorModel;

	/**
	 * the panel and label that displays the context-sensitive help
	 */
	private JPanel hint;
	private JLabel hintLabel;

	/**
	 * Useful color definitions
	 */
	private static final Color LIGHT_BLUE = new Color(0.9f, 0.9f, 1.0f);
	private static final Color LIGHT_YELLOW = new Color(1.0f, 1.0f, 0.9f);

	/**
	 * the reference to the abstract function which is currently inspected
	 */
	private AbstractFunction inspecting = null;

	/**
	 * Initializes the inspector panel and display
	 */
	public InspectorPanel() {
		// register as observer
		ViewRegistry.getInstance().addObserver(this);

		// caption
		caption = new JLabel(I18N.getInstance().getString(
				"View.InspectorPanel.NoSelection"));
		caption.setFont(caption.getFont().deriveFont(Font.BOLD));

		// display
		rowEditorModel = new RowEditorModel();
		display = new JRowTable(new InspectorPanelTableModel(), rowEditorModel);

		// display.getColumnModel().getColumn(0).setPreferredWidth(64);
		// display.getColumnModel().getColumn(1).setPreferredWidth(64);

		display.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// ensure clicking results in an edit
				int row = display.rowAtPoint(e.getPoint());
				display.editCellAt(row, 1);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		display.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// show applicable hint
				if (inspecting != null) {
					int row = display.rowAtPoint(e.getPoint());
					if (row == -1) {
						setHintText(inspecting.getDescription());
					} else if (row == 0) {
						setHintText(inspecting.getActiveTask().getDescription());
					} else {
						if (row >= inspecting.getActiveTask().getParameters().length) {
							ViewRegistry.showException(
									this.getClass(),
									ExceptionType.ABNORMAL_BEHAVIOR,
									new Exception(
											I18N.getInstance()
													.getString(
															"View.InspectorPanel.ParamCountException")));
						}
						setHintText(inspecting.getActiveTask().getParameters()[row - 1]
								.getDescription());
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

		display.setDefaultRenderer(String.class,
				new InspectorPanelTableRenderer());

		// hint
		hint = new JPanel();
		hint.setBackground(InspectorPanel.LIGHT_YELLOW);

		hintLabel = new JLabel();
		hint.add(hintLabel);

		// align this to look good
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(caption);
		add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(
				display), hint));
	}

	/**
	 * Sets the hint text
	 * 
	 * @param hintText
	 *            the hintText to set
	 */
	protected void setHintText(String hintText) {
		if ((hintText == null) || (hintText.isEmpty())) {
			hintText = I18N.getInstance().getString(
					"View.InspectorPanel.NoDescription");
		}
		hintLabel.setText("<html><body><p>" + hintText + "</p></body></html>");
	}

	@Override
	public void update(Observable o, Object arg) {
		if (inspecting != null) {
			caption.setText(inspecting.getFriendlyName());
			setHintText(inspecting.getDescription());
		} else {
			caption.setText(I18N.getInstance().getString(
					"View.InspectorPanel.NoSelection"));
			hintLabel.setText("");
		}
		repaint();
	}

	/**
	 * @param inspect
	 *            the inspected function to set
	 */
	public void inspect(AbstractFunction inspect) {
		this.inspecting = inspect;
	}

	/**
	 * The table model of the display table of the inspector panel.
	 * 
	 * @author tobias_kuhn
	 * 
	 */
	class InspectorPanelTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 6495527693821933683L;

		private String[] columns = new String[2];

		public InspectorPanelTableModel() {
			columns[0] = I18N.getInstance().getString("View.Parameter");
			columns[1] = I18N.getInstance().getString("View.Value");
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			if (inspecting == null) {
				return 0;
			} else {
				return 1 + inspecting.getActiveTask().getParameters().length;
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return (column == 1);
		}

		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (inspecting == null) {
				return null;
			} else {
				switch (column) {
				case 0:
					return inspecting.getActiveTask().getParameters()[row]
							.getFriendlyName();
				default:
					return inspecting.getActiveTask().getParameters()[row]
							.getValue();
				}
			}
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (inspecting != null) {
				ActionEvent ae = new ActionEvent(inspecting, row,
						aValue.toString());
				ActionRegistry.getInstance().get(EditPropertyAction.class)
						.actionPerformed(ae);
			}
		}

	} /* InspectorPanelTableModel */

	/**
	 * The custom cell renderer for the display table of the inspector panel.
	 * 
	 * @author tobias_kuhn
	 * 
	 */
	class InspectorPanelTableRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = -8005963595998602494L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);

			// some nice colors there
			if (column == 0) {
				c.setBackground(InspectorPanel.LIGHT_BLUE);
			} else {
				c.setBackground(Color.WHITE);
			}

			c.setForeground(Color.BLACK);

			// suppress borders
			// does not work for edited cells (in Metal LnF at least)
			if (c instanceof JComponent) {
				JComponent jc = (JComponent) c;
				jc.setBorder(null);
			}

			return c;
		}

	} /* InspectorPanelTableRenderer */

}