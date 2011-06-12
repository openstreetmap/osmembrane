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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;

import de.osmembrane.resources.Constants;
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
    public AboutDialog(Window owner) {
        super(owner);
        setLayout(new BorderLayout());

        JLabel splash = new JLabel(new ImageIcon(this.getClass().getResource(
                "/de/osmembrane/resources/images/splash.png")));
        add(splash, BorderLayout.NORTH);

        JEditorPane infoText = new JEditorPane();
        infoText.setContentType("text/html");
        infoText.setEditable(false);
        infoText.setText(I18N.getInstance().getString(
                "View.AboutDialog.BuildInfo", Constants.VERSION,
                Constants.REVISION_ID)
                + "\n" + I18N.getInstance().getString("View.AboutDialog.Info"));
        add(infoText, BorderLayout.CENTER);

        JButton okButton = new JButton(I18N.getInstance().getString("View.OK"));
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
