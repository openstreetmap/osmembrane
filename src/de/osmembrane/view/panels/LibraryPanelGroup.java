package de.osmembrane.view.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import de.osmembrane.model.AbstractFunction;
import de.osmembrane.model.AbstractFunctionGroup;

public class LibraryPanelGroup extends JPanel {

	/**
	 * The id this panel group has in its main Library panel (used for click
	 * handling calls)
	 */
	private int id;

	/**
	 * The header button of this panel group that can make it expandable or
	 * contractable
	 */
	private JButton headerButton;

	/**
	 * Initializes a new LibraryPanelGroup
	 * 
	 * @param lp
	 *            the parent LibraryPanel on which this group will be displayed
	 * @param afg
	 *            the {@link AbstractFunctionGroup} which this LibraryPanelGroup
	 *            represents
	 */
	public LibraryPanelGroup(final LibraryPanel lp, AbstractFunctionGroup afg) {
		// grid bag layout
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(8, 8, 8, 8);

		// header button
		headerButton = new JButton();
		headerButton.setText(afg.getFriendlyName());
		headerButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				lp.groupClicked(id);
			}
		});
		
		add(headerButton, gbc);

		// all functions available here
		for (AbstractFunction af : afg.getFunctions()) {

		}
	}

	/**
	 * @param id the id this panel group has in its main Library panel to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the height of this group's contents, if they are expanded
	 */
	public int getFullHeight() {
		return 0;
	}

	/**
	 * Sets the height for this group's contents
	 * @param newHeight 0, if contracted, getFullHeight() if expanded
	 */
	public void setHeight(int newHeight) {
		setSize(getWidth(), newHeight);
	}

}
