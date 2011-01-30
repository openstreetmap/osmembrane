package de.osmembrane.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicTreeUI.SelectionModelPropertyChangeHandler;
import javax.swing.table.DefaultTableModel;

import de.osmembrane.Application;
import de.osmembrane.model.pipeline.AbstractEnumValue;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.CopyType;
import de.osmembrane.model.pipeline.ParameterType;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;

/**
 * 
 * Simple dialog to edit special types of comma-separated lists with
 * AutoComplete functionality, plus save or load them.
 * 
 * @see Spezifikation.pdf, chapter 2.2
 * 
 * @author tobias_kuhn
 * 
 */
public class ListDialog extends AbstractDialog implements IListDialog {

	private static final long serialVersionUID = -1373885104364615162L;

	/**
	 * Whether or not to apply the changes made in the dialog
	 */
	private boolean applyChanges;

	/**
	 * The list parameter currently in edition
	 */
	private AbstractParameter listParam;

	/**
	 * The {@link JTable} to display all the list entries.
	 */
	private JTable editList;

	/**
	 * The model for editList
	 */
	private ListDialogTableModel editListModel;

	/**
	 * {@link JFileChooser} for saving and loading the model
	 */
	private JFileChooser fileChooser;

	/**
	 * Generates a new {@link ListDialog}.
	 */
	public ListDialog() {
		// set the basics up
		setLayout(new BorderLayout());

		setWindowTitle(I18N.getInstance().getString("View.ListDialog", ""));

		// ensure no editor when hiding
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				editList.removeEditor();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});

		// prepare file chooser
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return I18N.getInstance().getString(
						"View.ListDialog.FileTypeDescription");
			}

			@Override
			public boolean accept(File f) {
				return (f.getName().toLowerCase().endsWith(".txt") || f
						.isDirectory());
			}
		});

		// control buttons
		JButton okButton = new JButton(I18N.getInstance().getString("View.OK"));
		okButton.addKeyListener(returnButtonListener);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyChanges = true;
				editList.removeEditor();
				hideWindow();
			}
		});

		JButton cancelButton = new JButton(I18N.getInstance().getString(
				"View.Cancel"));
		cancelButton.addKeyListener(returnButtonListener);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyChanges = false;
				editList.removeEditor();
				hideWindow();
			}
		});

		JPanel buttonCtrlGrid = new JPanel(new GridLayout(1, 2));
		buttonCtrlGrid.add(okButton);
		buttonCtrlGrid.add(cancelButton);

		JPanel buttonCtrlFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonCtrlFlow.add(buttonCtrlGrid);

		add(buttonCtrlFlow, BorderLayout.SOUTH);

		// editing buttons
		JButton deleteButton = new JButton(I18N.getInstance().getString(
				"View.Delete"));
		deleteButton.addKeyListener(returnButtonListener);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rowsToDelete = editList.getSelectedRows();
				Arrays.sort(rowsToDelete);
				for (int i = rowsToDelete.length - 1; i >= 0; i--) {
					editListModel.delete(rowsToDelete[i]);
				}
			}
		});

		JButton clearButton = new JButton(I18N.getInstance().getString(
				"View.Clear"));
		clearButton.addKeyListener(returnButtonListener);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editListModel.regenerate("");
			}
		});

		JButton resetButton = new JButton(I18N.getInstance().getString(
				"View.Reset"));
		resetButton.addKeyListener(returnButtonListener);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editListModel.regenerate(listParam.getValue());
			}
		});

		JButton loadButton = new JButton(I18N.getInstance().getString(
				"View.Load"));
		loadButton.addKeyListener(returnButtonListener);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(ListDialog.this) == JFileChooser.APPROVE_OPTION) {
					try {
						FileReader fr = new FileReader(fileChooser
								.getSelectedFile());
						BufferedReader br = new BufferedReader(fr);

						StringBuilder read = new StringBuilder();
						String line;
						while ((line = br.readLine()) != null) {
							read.append(line);
						}
						editListModel.regenerate(read.toString());

						br.close();
						fr.close();
					} catch (IOException e1) {
						Application.handleException(e1);
					}
				}
			}
		});

		JButton saveButton = new JButton(I18N.getInstance().getString(
				"View.Save"));
		saveButton.addKeyListener(returnButtonListener);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(ListDialog.this) == JFileChooser.APPROVE_OPTION) {
					try {
						String saveTo = fileChooser.getSelectedFile()
								.getAbsolutePath();
						if (!saveTo.toLowerCase().endsWith("*.txt")) {
							saveTo += ".txt";
						}
						FileWriter fw = new FileWriter(saveTo);

						fw.write(editListModel.getContent());

						fw.close();
					} catch (IOException e1) {
						Application.handleException(e1);
					}
				}
			}
		});

		JPanel buttonEditGrid = new JPanel(new GridLayout(7, 1));
		buttonEditGrid.add(deleteButton);
		buttonEditGrid.add(clearButton);
		buttonEditGrid.add(resetButton);
		buttonEditGrid.add(new JLabel());
		buttonEditGrid.add(loadButton);
		buttonEditGrid.add(saveButton);

		JPanel buttonEditFlow = new JPanel(new FlowLayout(FlowLayout.LEADING));
		buttonEditFlow.add(buttonEditGrid);
		add(buttonEditFlow, BorderLayout.EAST);

		editListModel = new ListDialogTableModel();
		editList = new JTable(editListModel);
		editList.setPreferredSize(new Dimension(640, 480));
		editList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel edit = new JPanel(new BorderLayout());
		edit.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		edit.add(new JScrollPane(editList), BorderLayout.CENTER);

		add(edit, BorderLayout.CENTER);

		pack();
		centerWindow();
	}

	@Override
	public void open(AbstractParameter list) {
		this.listParam = list;
		this.editListModel.regenerate(list.getValue());
		this.applyChanges = false;

		setWindowTitle(I18N.getInstance().getString("View.ListDialog",
				list.getListType()));
		showWindow();
	}

	@Override
	public boolean shallApplyChanges() {
		return this.applyChanges;
	}

	@Override
	public String getEdits() {
		return editListModel.getContent();
	}

	/**
	 * Table model to directly access the changes made in the list.
	 * 
	 * @author tobias_kuhn
	 * 
	 */
	class ListDialogTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 6174982966160493569L;

		/**
		 * Stores all the currently set parameters in the model
		 */
		private List<String> parameters;

		/**
		 * Regenerates the parameters list from params
		 * 
		 * @param params
		 */
		public void regenerate(String params) {
			if (params == null) {
				params = new String();
			}

			parameters = new ArrayList<String>();
			for (String param : params.split(",")) {
				parameters.add(param.trim());
			}

			fireTableDataChanged();
		}

		/**
		 * Removes the entry with index index. Does nothing, if index does not
		 * exist.
		 * 
		 * @param index
		 */
		public void delete(int index) {
			if ((index >= 0) && (index < parameters.size())) {
				parameters.remove(index);
				fireTableDataChanged();
			}
		}

		/**
		 * @return the parameters list from parameters
		 */
		public String getContent() {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < parameters.size() - 1; i++) {
				result.append(parameters.get(i) + ",");
			}
			if (parameters.size() > 0) {
				result.append(parameters.get(parameters.size() - 1));
			}
			return result.toString();
		}

		@Override
		public int getRowCount() {
			if (parameters != null) {
				return parameters.size()
						+ (int) (editList.getHeight() / (double) editList
								.getRowHeight());
			} else {
				return 0;
			}
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public String getColumnName(int column) {
			return I18N.getInstance().getString("View.Parameter");
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if ((parameters != null) && (row >= 0) && (row < parameters.size())) {
				return parameters.get(row);
			} else {
				return null;
			}
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if ((row >= 0) && (row < parameters.size())) {
				parameters.set(row, aValue.toString());
			} else {
				parameters.add(aValue.toString());
				fireTableDataChanged();
			}
		}

	}

}