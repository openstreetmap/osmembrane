package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.EditPropertyAction;
import de.osmembrane.controller.events.ContainingFunctionChangeParameterEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractTask;
import de.osmembrane.model.pipeline.PipelineObserverObject;
import de.osmembrane.tools.I18N;
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
	private JLabel functionName;

	/**
	 * the table that displays the data of the function
	 */
	private JRowTable propertyTable;
	private InspectorPanelTableModel propertyTableModel;
	private RowEditorModel rowEditorModel;

	/**
	 * the model for the combo box to chose the tasks
	 */
	private InspectorPanelTableTaskComboBoxModel taskComboModel;

	/**
	 * the panel and label that displays the context-sensitive help
	 */
	private JPanel hint;
	private JTextArea hintLabel;

	/**
	 * Useful color definitions
	 */
	private static final Color LIGHT_BLUE = new Color(0.9f, 0.9f, 1.0f);
	private static final Color LIGHT_YELLOW = new Color(1.0f, 1.0f, 0.9f);

	/**
	 * An empty hint that is actually valid. (The hint panel normally checks for
	 * == null and .isEmpty())
	 */
	protected static final String VALID_EMPTY_HINT = " ";

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
		functionName = new JLabel(I18N.getInstance().getString(
				"View.Inspector.NoSelection"));
		functionName.setFont(functionName.getFont().deriveFont(Font.BOLD,
				1.2f * functionName.getFont().getSize()));
		functionName.setAlignmentX(Component.CENTER_ALIGNMENT);

		// display
		taskComboModel = new InspectorPanelTableTaskComboBoxModel();

		rowEditorModel = new RowEditorModel();
		rowEditorModel.addEditorRow(0, new DefaultCellEditor(new JComboBox(
				taskComboModel)));
		propertyTableModel = new InspectorPanelTableModel();
		propertyTable = new JRowTable(propertyTableModel, rowEditorModel);

		propertyTable.getColumnModel().getColumn(0).setPreferredWidth(64);
		propertyTable.getColumnModel().getColumn(1).setPreferredWidth(64);

		propertyTable.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// ensure clicking results in an edit
				int row = propertyTable.rowAtPoint(e.getPoint());
				propertyTable.editCellAt(row, 1);
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

		propertyTable.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// show applicable hint
				if (inspecting != null) {
					int row = propertyTable.rowAtPoint(e.getPoint());

					if (row == -1) {
						setHintText(inspecting.getDescription());
					} else if (row == 0) {
						setHintText(inspecting.getActiveTask().getDescription());
					} else {
						if (row > inspecting.getActiveTask().getParameters().length) {
							Application
									.handleException(new ControlledException(
											this,
											ExceptionSeverity.UNEXPECTED_BEHAVIOR,
											I18N.getInstance()
													.getString(
															"View.Inspector.ParamCountException")));
						}
						setHintText(inspecting.getActiveTask().getParameters()[row - 1]
								.getDescription());
					}
				} /* inspecting != null */
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

		propertyTable.setDefaultRenderer(String.class,
				new InspectorPanelTableRenderer());

		// hint
		hint = new JPanel();
		hint.setBackground(InspectorPanel.LIGHT_YELLOW);
		hint.setLayout(new GridLayout(1, 1));

		hintLabel = new JTextArea();
		hintLabel.setEditable(false);
		hintLabel.setOpaque(false);
		hintLabel.setBorder(null);
		hintLabel.setWrapStyleWord(true);
		hintLabel.setLineWrap(true);
		hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		hint.add(hintLabel);

		// align this to look good
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(functionName);

		JSplitPane propertiesAndHints = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, true,
				new JScrollPane(propertyTable), (hint));
		propertiesAndHints.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(propertiesAndHints);
	}

	/**
	 * Sets the hint text
	 * 
	 * @param hintText
	 *            the hintText to set
	 */
	protected void setHintText(String hintText) {
		if ((hintText == null) || (hintText.isEmpty())) {
			hintText = I18N.getInstance().getString("View.Inspector.NoHint");
		}
		hintLabel.setText("<html><center><br /><p>" + hintText
				+ "</p></center></html>");
	}

	@Override
	public void update(Observable o, Object arg) {
		if ((inspecting != null) && (arg instanceof PipelineObserverObject)) {
			PipelineObserverObject poo = (PipelineObserverObject) arg;

			switch (poo.getType()) {
			case ADD:
			case CHANGE:
				if (poo.getChangedFunction().equals(inspecting)) {
					inspect(inspecting);
					repaint();
				}
				break;
			case DELETE:
				if (!poo.getChangedFunction().equals(inspecting)) {
					break;
				}
			case FULLCHANGE:
				inspect(null);
				break;
			}
		}
	}

	/**
	 * @param inspect
	 *            the inspected function to set
	 */
	public void inspect(AbstractFunction inspect) {
		this.inspecting = inspect;

		if (inspect == null) {
			functionName.setText(I18N.getInstance().getString(
					"View.Inspector.NoSelection"));
		} else {
			functionName.setText(inspect.getFriendlyName());
		}

		propertyTableModel.fireTableDataChanged();
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
					if (row > 0) {
						return inspecting.getActiveTask().getParameters()[row - 1]
								.getFriendlyName();
					} else {
						return I18N.getInstance().getString("View.Task");
					}
				default:
					if (row > 0) {
						return inspecting.getActiveTask().getParameters()[row - 1]
								.getValue();
					} else {
						return inspecting.getActiveTask().getFriendlyName();
					}
				}
			} /* else */
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (inspecting != null) {
				ContainingFunctionChangeParameterEvent cfcpe = new ContainingFunctionChangeParameterEvent(
						this, inspecting);

				if (row > 0) {
					cfcpe.setChangedParameter(row - 1);
					cfcpe.setNewParameterValue(aValue.toString());

					ActionRegistry.getInstance().get(EditPropertyAction.class)
							.actionPerformed(cfcpe);
				}
			}
		}
	} /* InspectorPanelTableModel */

	/**
	 * Model for the combo box of the inspector panel's table. Is in a contest
	 * with {@link ContainingFunctionChangeParameterEvent} for the longest name
	 * in the project.
	 * 
	 * @author tobias_kuhn
	 * 
	 */
	class InspectorPanelTableTaskComboBoxModel extends DefaultComboBoxModel {

		private static final long serialVersionUID = 5611487798519928339L;

		@Override
		public int getSize() {
			if (inspecting == null) {
				return 0;
			} else {
				return inspecting.getAvailableTasks().length;
			}
		}

		@Override
		public Object getElementAt(int index) {
			if (inspecting == null) {
				return null;
			} else {
				return inspecting.getAvailableTasks()[index].getFriendlyName();
			}
		}

		@Override
		public Object getSelectedItem() {
			if (inspecting == null) {
				return null;
			} else {
				// here we pray, Java uses .equals()
				return inspecting.getActiveTask().getFriendlyName();
			}
		}

		@Override
		public void setSelectedItem(Object anObject) {
			if ((inspecting != null) && (anObject != null)) {
				ContainingFunctionChangeParameterEvent cfcpe = new ContainingFunctionChangeParameterEvent(
						this, inspecting);

				for (AbstractTask at : inspecting.getAvailableTasks()) {
					if (anObject.equals(at.getFriendlyName())
							&& !at.equals(inspecting.getActiveTask())) {
						cfcpe.setNewTask(at);

						ActionRegistry.getInstance()
								.get(EditPropertyAction.class)
								.actionPerformed(cfcpe);

						break;
					}
				}
			}
		} /* setSelectedItem */

	}

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

			// suppress "selected" borders
			// does not work for edited cells (in Metal LnF at least)
			if (c instanceof JComponent) {
				JComponent jc = (JComponent) c;
				jc.setBorder(null);
			}

			return c;
		}

	} /* InspectorPanelTableRenderer */

}