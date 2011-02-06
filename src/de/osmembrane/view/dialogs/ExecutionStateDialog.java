package de.osmembrane.view.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.interfaces.IExecutionStateDialog;

/**
 * The dialog that is displayed when an external program is executed.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExecutionStateDialog extends AbstractDialog implements
		IExecutionStateDialog {

	private static final long serialVersionUID = 956559876768946717L;

	private JTextField stateField;

	private JProgressBar progress;

	private JTextArea lines;

	/**
	 * Creates a new {@link ExecutionStateDialog}.
	 */
	public ExecutionStateDialog() {
		setLayout(new BorderLayout());

		// state stuff
		JPanel top = new JPanel();
		top.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		top.add(new JLabel(I18N.getInstance().getString("View.State") + ":"),
				gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		stateField = new JTextField();
		stateField.setEditable(false);
		top.add(stateField, gbc);

		add(top, BorderLayout.NORTH);

		// progress & lines
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		progress = new JProgressBar(0, 100);
		content.add(progress, BorderLayout.NORTH);

		lines = new JTextArea(25, 80);
		lines.setEditable(false);
		lines.setLineWrap(true);
		lines.setWrapStyleWord(true);

		Map<Attribute, String> fontAttrib = new HashMap<Attribute, String>();
		fontAttrib.put(TextAttribute.FAMILY, Font.MONOSPACED);
		lines.setFont(lines.getFont().deriveFont(fontAttrib));

		content.add(lines, BorderLayout.CENTER);

		add(new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		// buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton cancelButton = new JButton(I18N.getInstance().getString(
				"View.Cancel"));
		cancelButton.addKeyListener(returnButtonListener);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hideWindow();
				for (WindowListener wl : getWindowListeners()) {
					wl.windowClosing(new WindowEvent(ExecutionStateDialog.this,
							WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		buttons.add(cancelButton);

		add(buttons, BorderLayout.SOUTH);
		setTitle(I18N.getInstance().getString("View.ExecutionStateDialog"));

		pack();
		centerWindow();
	}

	@Override
	public void setState(String state) {
		stateField.setText(state);
	}

	@Override
	public void setProgress(int progress) {
		this.progress.setValue(progress);
	}

	@Override
	public void addOutputLine(String outputLine) {
		if (lines.getText().isEmpty()) {
			lines.setText(outputLine);
		} else {
			lines.setText(lines.getText()
					+ System.getProperty("line.separator") + outputLine);
		}
		lines.setCaretPosition(lines.getText().length() - 1);
	}

	@Override
	public void clear() {
		stateField.setText("");
		progress.setValue(0);
		lines.setText("");
	}

}
