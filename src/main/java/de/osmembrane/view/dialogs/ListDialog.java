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

package de.osmembrane.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import org.openstreetmap.josm.gui.tagging.ac.AutoCompletingComboBox;

import de.osmembrane.Application;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.preset.PresetItem;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.interfaces.IListDialog;

/**
 * 
 * Simple dialog to edit special types of comma-separated lists with
 * AutoComplete functionality, plus save or load them.
 * 
 * @see "Spezifikation.pdf, chapter 2.2 (German)"
 * 
 * @author tobias_kuhn
 * 
 */
public class ListDialog extends AbstractDialog implements IListDialog {

    private static final long serialVersionUID = -1373885104364615162L;

    /**
     * Allocates the correct type for the listType
     * 
     * @author tobias_kuhn
     * 
     */
    private enum ListType {
        INVALID, NODE, WAY
    };

    /**
     * Allocates the correct content type for the listContentType
     * 
     * @author tobias_kuhn
     * 
     */
    private enum ListContentType {
        INVALID, KEY, KEY_VALUE
    }

    /**
     * Whether or not to apply the changes made in the dialog
     */
    private boolean applyChanges;

    /**
     * The list parameter currently in edition
     */
    private AbstractParameter listParam;

    /**
     * The ListType of the list
     */
    private ListType listType;

    /**
     * The ListContentType of the list
     */
    private ListContentType listContentType;

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
     * Field for entering new entries
     */
    private AutoCompletingComboBox editField;

    /**
     * Generates a new {@link ListDialog}.
     */
    public ListDialog(Window owner) {
        super(owner);

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
                // close the annoying JTable editor when we're leaving
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
        File startDir = new File((String) ModelProxy.getInstance()
                .getSettings()
                .getValue((SettingType.DEFAULT_WORKING_DIRECTORY)));
        fileChooser = new JFileChooser(startDir);
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
        final JButton addButton = new JButton(I18N.getInstance().getString(
                "View.Add"));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // reset field, add to model
                editListModel.insert(editField.getSelectedItem().toString());
                editField.setSelectedItem("");
            }
        });

        final JButton deleteButton = new JButton(I18N.getInstance().getString(
                "View.Delete"));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // delete rows from model
                int[] rowsToDelete = editList.getSelectedRows();
                Arrays.sort(rowsToDelete);
                for (int i = rowsToDelete.length - 1; i >= 0; i--) {
                    editListModel.delete(rowsToDelete[i]);
                }
            }
        });

        JButton clearButton = new JButton(I18N.getInstance().getString(
                "View.Clear"));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editListModel.regenerate("");
            }
        });

        JButton resetButton = new JButton(I18N.getInstance().getString(
                "View.Reset"));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // regenerate model with parameter data
                editListModel.regenerate(listParam.getValue());
            }
        });

        JButton loadButton = new JButton(I18N.getInstance().getString(
                "View.Load"));
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // load from file
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
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // save to file
                if (fileChooser.showSaveDialog(ListDialog.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String saveTo = fileChooser.getSelectedFile()
                                .getAbsolutePath();
                        if (!saveTo.toLowerCase().endsWith(".txt")) {
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
        buttonEditGrid.add(addButton);
        buttonEditGrid.add(deleteButton);
        buttonEditGrid.add(clearButton);
        buttonEditGrid.add(resetButton);
        buttonEditGrid.add(new JLabel());
        buttonEditGrid.add(loadButton);
        buttonEditGrid.add(saveButton);

        JPanel buttonEditFlow = new JPanel(new FlowLayout(FlowLayout.LEADING));
        buttonEditFlow.add(buttonEditGrid);
        add(buttonEditFlow, BorderLayout.EAST);

        // data holders
        editListModel = new ListDialogTableModel();
        editList = new JTable(editListModel);
        editList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        editList.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // do not edit on delete, do delete the item
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    editList.removeEditor();
                    deleteButton.doClick();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });

        JScrollPane editPane = new JScrollPane(editList);
        editPane.setPreferredSize(new Dimension(640, 480));

        JPanel edit = new JPanel(new BorderLayout());
        edit.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        edit.add(editPane, BorderLayout.CENTER);

        editField = new AutoCompletingComboBox();
        editField.setEditable(true);
        edit.add(editField, BorderLayout.NORTH);

        add(edit, BorderLayout.CENTER);

        pack();
        centerWindow();
    }

    @Override
    public void open(AbstractParameter list) {
        this.listParam = list;
        this.editListModel.regenerate(list.getValue());
        this.applyChanges = false;

        // determine listType and listContentType
        if (listParam.getListType().toLowerCase().contains("node")) {
            this.listType = ListType.NODE;
        } else if (listParam.getListType().toLowerCase().contains("way")) {
            this.listType = ListType.WAY;
        } else {
            this.listType = ListType.INVALID;
        }

        // note: order important
        if (listParam.getListType().toLowerCase().contains("key")
                && listParam.getListType().toLowerCase().contains("value")) {
            this.listContentType = ListContentType.KEY_VALUE;
        } else if (listParam.getListType().toLowerCase().contains("key")) {
            this.listContentType = ListContentType.KEY;
        } else {
            this.listContentType = ListContentType.INVALID;
        }

        this.editField.setSelectedItem(null);
        this.editField.setPossibleItems(generateAutoCompletionList());
        this.editField.requestFocus();

        setWindowTitle(I18N.getInstance().getString("View.ListDialog",
                list.getListType()));
        showWindow();
    }

    private Collection<String> generateAutoCompletionList() {
        PresetItem[] items = null;
        if (listType == ListType.NODE) {
            if (listContentType == ListContentType.KEY) {
                items = ModelProxy.getInstance().getPreset().getNodeKeys();
            } else if (listContentType == ListContentType.KEY_VALUE) {
                items = ModelProxy.getInstance().getPreset().getNodes();
            }
        } else if (listType == ListType.WAY) {
            if (listContentType == ListContentType.KEY) {
                items = ModelProxy.getInstance().getPreset().getWayKeys();
            } else if (listContentType == ListContentType.KEY_VALUE) {
                items = ModelProxy.getInstance().getPreset().getWays();
            }
        }

        if (items == null) {
            return new ArrayList<String>(0);
        }

        List<String> result = new ArrayList<String>(items.length);

        for (PresetItem i : items) {
            String s;
            if (listContentType == ListContentType.KEY_VALUE) {
                s = i.getKeyValue();
            } else {
                s = i.getKey();
            }
            int index = Collections.binarySearch(result, s);
            if (index >= 0) {
                continue;
            }

            result.add(~index, s);
        }

        return result;
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
     * Sets the current editList editor value to full and selects all parts of
     * full, that are after contentLen. Needs to be synchronized, so setText()
     * and setSelection() are performed somewhat atomic. Still fails for very
     * quick typings.
     * 
     * @param full
     *            the String you most likely want to enter
     * @param contentLen
     *            the length of the content you have entered so far
     */
    public synchronized void setEditorValue(String full, int contentLen) {
        JTextField editorField = (JTextField) editField.getEditor()
                .getEditorComponent();
        editorField.setText(full);
        editorField.setSelectionStart(contentLen);
        editorField.setSelectionEnd(full.length());
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
                String trimmed = param.trim();
                if (!trimmed.isEmpty()) {
                    parameters.add(trimmed);
                }
            }

            fireTableDataChanged();
        }

        /**
         * inserts text
         * 
         * @param text
         */
        public void insert(String text) {
            if (text.isEmpty()) {
                return;
            }
            parameters.add(text);
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
                return parameters.size();
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

    } /* ListDialogTableModel */
}
