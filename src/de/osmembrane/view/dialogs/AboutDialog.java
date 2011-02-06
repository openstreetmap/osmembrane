package de.osmembrane.view.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;

import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;

/**
 * The about dialog informing people about the program.
 * 
 * @author tobias_kuhn
 * 
 */
public class AboutDialog extends AbstractDialog {

	private static final long serialVersionUID = 525351301396477062L;

	/**
	 * Creates a new {@link AboutDialog}.
	 */
	public AboutDialog() {
		setLayout(new BorderLayout());
		
		JLabel splash = new JLabel(new ImageIcon(this.getClass().getResource(
				"/de/osmembrane/resources/images/splash.png")));
		add(splash, BorderLayout.NORTH);
		
		JEditorPane infoText = new JEditorPane();
		infoText.setContentType("text/html");
		infoText.setEditable(false);
		infoText.setText(I18N.getInstance().getString("View.AboutDialog.Info"));		
		add(infoText, BorderLayout.CENTER);

		JButton okButton = new JButton(I18N.getInstance().getString("View.OK"));
		okButton.addKeyListener(returnButtonListener);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hideWindow();
			}
		});
		add(okButton, BorderLayout.SOUTH);
		
		setTitle(I18N.getInstance().getString("View.AboutDialog"));

		pack();
		centerWindow();
	}

}
