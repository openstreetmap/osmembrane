package de.osmembrane.view.panels;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.EditBoundingBoxPropertyAction;
import de.osmembrane.controller.actions.EditPropertyAction;
import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.controller.events.ContainingFunctionChangeParameterEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.AbstractTask;
import de.osmembrane.model.pipeline.PipelineObserverObject;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.components.JRowTable;
import de.osmembrane.view.components.JTextFieldWithButton;
import de.osmembrane.view.components.JTextFieldWithButtonCellEditor;
import de.osmembrane.view.components.RowEditorModel;

/**
 * The inspector panel component to realize the function inspector.
 * 
 * @see Spezifikation.pdf, chapter 2.1.6
 * 
 * @author tobias_kuhn
 * 
 */
public class InspectorPanel extends JPanel implements Observer {

	private static final long serialVersionUID = -4331036066478472018L;

	/**
	 * the {@link JLabel} that shows the function name
	 */
	private JLabel functionName;

	/**
	 * the {@link JRowTable} that displays the data of the function along with
	 * its {@link InspectorPanelTableModel} and {@link RowEditorModel}.
	 */
	private JRowTable propertyTable;
	private InspectorPanelTableModel propertyTableModel;
	private RowEditorModel rowEditorModel;

	/**
	 * the {@link InspectorPanelTableTaskComboBoxModel} for the combo box to
	 * choose the tasks
	 */
	private InspectorPanelTableTaskComboBoxModel taskComboModel;

	/**
	 * the {@link JPanel} and {@link JLabel} that display the context-sensitive
	 * help
	 */
	private JPanel hint;
	private JEditorPane hintLabel;

	/**
	 * Useful {@link Color} definitions
	 */
	private static final Color LIGHT_BLUE = new Color(0.9f, 0.9f, 1.0f);
	private static final Color LIGHTER_BLUE = new Color(0.95f, 0.95f, 1.0f);
	private static final Color LIGHT_WHITE = new Color(0.95f, 0.95f, 0.95f);
	private static final Color LIGHTER_WHITE = new Color(1.0f, 1.0f, 1.0f);

	private static final Color LIGHT_YELLOW = new Color(1.0f, 1.0f, 0.9f);

	/**
	 * An empty hint that is actually valid. (The hint panel normally checks for
	 * == null and .isEmpty())
	 */
	protected static final String VALID_EMPTY_HINT = " ";

	/**
	 * the reference to the {@link AbstractFunction} which is currently
	 * inspected
	 */
	private AbstractFunction inspecting = null;

	/**
	 * Initializes the {@link InspectorPanel} and display
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
		propertyTableModel = new InspectorPanelTableModel();
		propertyTable = new JRowTable(propertyTableModel, rowEditorModel);

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

		propertyTable.setDefaultRenderer(JComponent.class,
				new InspectorPanelTableRenderer());
		// for nicer ComboBoxes
		propertyTable.setRowHeight((int) (1.2 * propertyTable.getRowHeight()));

		// hint
		hint = new JPanel();
		hint.setBackground(InspectorPanel.LIGHT_YELLOW);
		hint.setLayout(new GridLayout(1, 1));

		hintLabel = new JEditorPane();
		hintLabel.setEditable(false);
		hintLabel.setOpaque(false);
		hintLabel.setBorder(null);
		hintLabel.setContentType("text/html");
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

		StringBuilder sb = new StringBuilder();
		FontMetrics fm = hintLabel.getFontMetrics(hintLabel.getFont());
		int lineWidth = 0;

		for (String word : hintText.split("\\s")) {
			int thisWidth = fm.stringWidth(word + " ");

			// if this line is wider than possible, make a new line
			if (lineWidth + thisWidth >= hint.getWidth() - 8) {
				sb.append("<br />" + word);
				lineWidth = 0;
			} else {
				// append
				lineWidth += thisWidth;
				sb.append(word + " ");
			}
		}

		hintLabel.setText("<html><center><br /><p>" + sb.toString()
				+ "</p></center></html>");
	}

	@Override
	public void update(Observable o, Object arg) {
		if ((inspecting != null) && (arg instanceof PipelineObserverObject)) {
			PipelineObserverObject poo = (PipelineObserverObject) arg;

			switch (poo.getType()) {
			case ADD_FUNCTION:
			case CHANGE_FUNCTION:
				if (poo.getChangedFunction().equals(inspecting)) {
					inspect(inspecting);
					repaint();
				}
				break;
			case DELETE_FUNCTION:
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
		rowEditorModel.clear();
		propertyTable.removeEditor();

		this.inspecting = inspect;

		if (inspect == null) {
			functionName.setText(I18N.getInstance().getString(
					"View.Inspector.NoSelection"));
		} else {
			functionName.setText(inspect.getFriendlyName());

			rowEditorModel.setEditorRow(0, new DefaultCellEditor(new JComboBox(
					taskComboModel)));

			// find the appropriate RowEditors for the parameters
			for (int i = 0; i < inspect.getActiveTask().getParameters().length; i++) {
				final AbstractParameter ap = inspect.getActiveTask()
						.getParameters()[i];

				switch (ap.getType()) {

				case ENUM:
					InspectorPanelTableCustomEnumComboBoxModel iptcecbm = new InspectorPanelTableCustomEnumComboBoxModel(
							ap);
					DefaultCellEditor dceEnum = new DefaultCellEditor(
							new JComboBox(iptcecbm));
					rowEditorModel.setEditorRow(i + 1, dceEnum);
					break;

				case BOOLEAN:
					JCheckBox jcb = new JCheckBox();
					jcb.setModel(new InspectorPanelTableBooleanCheckBoxModel(ap));
					DefaultCellEditor dceBoolean = new DefaultCellEditor(jcb);
					rowEditorModel.setEditorRow(i + 1, dceBoolean);
					break;

				case BBOX:
					JTextFieldWithButton jtfwb = new JTextFieldWithButton(
							ap.getValue(), "...");
					jtfwb.addButtonActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Action a = ActionRegistry.getInstance().get(
									EditBoundingBoxPropertyAction.class);
							ContainingEvent ce = new ContainingEvent(this, ap);
							a.actionPerformed(ce);
						}
					});
					rowEditorModel.setEditorRow(i + 1,
							new JTextFieldWithButtonCellEditor(jtfwb));
					break;

				/*
				 * geht nicht: gibts nicht (doesn't work: doesn't exist) case
				 * LIST: break;
				 */

				default:
					JTextField textField = new JTextField(ap.getValue());
					textField.setOpaque(true);
					textField.setBorder(null);
					DefaultCellEditor dce = new DefaultCellEditor(textField);
					dce.setClickCountToStart(1);
					rowEditorModel.setEditorRow(i + 1, dce);

				}
			} /* for */
		}

		propertyTableModel.fireTableDataChanged();
	}

	/**
	 * The table model of the display table of the {@link InspectorPanel}.
	 * 
	 * @author tobias_kuhn
	 * 
	 */
	class InspectorPanelTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 6495527693821933683L;

		private String[] columns = new String[2];

		/**
		 * Creates a new {@link InspectorPanelTableModel	
		 */
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
			return JComponent.class;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (inspecting == null) {
				return null;
			} else {
				Object result = null;

				switch (column) {
				case 0:
					// property descriptions
					if (row > 0) {
						result = inspecting.getActiveTask().getParameters()[row - 1]
								.getFriendlyName();
					} else {
						result = I18N.getInstance().getString("View.Task");
					}
					break;
				default:
					// property values
					TableCellEditor tce = rowEditorModel.getEditorRow(row);

					boolean selected = (propertyTable.getSelectedRow() == row)
							&& (propertyTable.getSelectedColumn() == column);

					result = tce.getTableCellEditorComponent(propertyTable,
							tce.getCellEditorValue(), selected, row, column);

					if (result instanceof JTextField) {
						result = ((JTextField) result).getText();
					}
				}

				return result;
			} /* else */
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (inspecting != null) {
				ContainingFunctionChangeParameterEvent cfcpe = new ContainingFunctionChangeParameterEvent(
						this, inspecting);

				if ((row > 0) && (aValue instanceof String)) {
					cfcpe.setChangedParameter(inspecting.getActiveTask()
							.getParameters()[row - 1]);
					cfcpe.setNewParameterValue(aValue.toString());

					ActionRegistry.getInstance().get(EditPropertyAction.class)
							.actionPerformed(cfcpe);
				}
			}
		}
	} /* InspectorPanelTableModel */

	/**
	 * Model for the task's combo box of the {@link InspectorPanel}'s table. Is
	 * in a contest with {@link ContainingFunctionChangeParameterEvent} and
	 * {@link InspectorPanelTableCustomEnumComboBoxModel} for the longest name
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
				} /* for tasks */
			}
		}

	} /* InspectorPanelTableTaskComboBoxModel */

	/**
	 * Model for an enum property of the combo box of the {@link InspectorPanel}
	 * 's table. Is in a contest with
	 * {@link ContainingFunctionChangeParameterEvent} and
	 * {@link InspectorPanelTableTaskComboBoxModel} for the longest name in the
	 * project.
	 */
	class InspectorPanelTableCustomEnumComboBoxModel extends
			DefaultComboBoxModel {

		private static final long serialVersionUID = 5695998035505672596L;

		/**
		 * the parameter with enum to be edited
		 */
		private AbstractParameter param;

		/**
		 * Creates a new {@link InspectorPanelTableCustomEnumComboBoxModel}
		 * 
		 * @param param
		 *            the enum parameter to be edited
		 */
		public InspectorPanelTableCustomEnumComboBoxModel(
				AbstractParameter param) {
			this.param = param;
		}

		@Override
		public int getSize() {
			return param.getEnumValue().length;
		}

		@Override
		public Object getElementAt(int index) {
			return param.getEnumValue()[index].getValue();
		}

		@Override
		public Object getSelectedItem() {
			return param.getValue();
		}

		@Override
		public void setSelectedItem(Object anObject) {
			if ((anObject != null) && (anObject instanceof String)) {
				ContainingFunctionChangeParameterEvent cfcpe = new ContainingFunctionChangeParameterEvent(
						this, inspecting);

				cfcpe.setChangedParameter(param);
				cfcpe.setNewParameterValue((String) anObject);

				ActionRegistry.getInstance().get(EditPropertyAction.class)
						.actionPerformed(cfcpe);
			}
		}

	} /* InspectorPanelTableCustomEnumComboBoxModel */

	/**
	 * /** Model for a boolean property of the check box of the
	 * {@link InspectorPanel} 's table. I suppose by now, you know these
	 * abominations.
	 * 
	 * @author tobias_kuhn
	 * 
	 */
	class InspectorPanelTableBooleanCheckBoxModel extends ToggleButtonModel {

		private static final long serialVersionUID = -3716607547245684756L;

		private AbstractParameter param;

		public InspectorPanelTableBooleanCheckBoxModel(AbstractParameter param) {
			this.param = param;
		}

		@Override
		public boolean isSelected() {
			return param.getValue().equals(Boolean.TRUE.toString());
		}

		@Override
		public void setSelected(boolean b) {
			Boolean bool = Boolean.valueOf(b);
			if (param.getValue().equals(bool.toString())) {
				return;
			}

			ContainingFunctionChangeParameterEvent cfcpe = new ContainingFunctionChangeParameterEvent(
					this, inspecting);

			cfcpe.setChangedParameter(param);
			cfcpe.setNewParameterValue(bool.toString());

			ActionRegistry.getInstance().get(EditPropertyAction.class)
					.actionPerformed(cfcpe);
		}

	}

	/**
	 * The custom cell renderer for the display table of the
	 * {@link InspectorPanel}.
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
			Component c;

			c = super.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);

			if (value instanceof JComboBox) {
				c = (Component) value;
			} else if (value instanceof JTextFieldWithButton) {
				c = (Component) value;
			} else if (value instanceof JTextField) {
				c = (Component) value;
			} else if (value instanceof JCheckBox) {
				c = (Component) value;
			}

			// some nice colors there
			if (column == 0) {
				c.setBackground((row % 2 == 0) ? InspectorPanel.LIGHT_BLUE
						: InspectorPanel.LIGHT_BLUE);
			} else {
				c.setBackground((row % 2 == 0) ? InspectorPanel.LIGHT_WHITE
						: InspectorPanel.LIGHTER_WHITE);
			}

			c.setForeground(Color.BLACK);

			// suppress "selected" borders
			// does not work for edited cells (in Metal LnF at least)
			if (c instanceof JComponent) {
				JComponent jc = (JComponent) c;
				jc.setBorder(null);
				jc.setOpaque(true);
			}

			return c;
		}

	} /* InspectorPanelTableRenderer */

}