package de.osmembrane.view.panels;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import de.osmembrane.model.AbstractFunction;
import de.osmembrane.model.AbstractFunctionGroup;

public class LibraryPanelGroup extends JPanel {
	
	/**
	 * The id this panel group has in its main Library panel
	 * (used for click handling calls)
	 */
	private int id;
	
	/**
	 * The header button of this panel group that can make it
	 * expandable or contractable
	 */
	private JButton headerButton;
	
	public LibraryPanelGroup(final LibraryPanel lp, AbstractFunctionGroup afg) {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(8, 8, 8, 8);
		
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
		
		for (AbstractFunction af : afg.getFunctions()) {
			
		}
	}

	public void setId(int id) {
		this.id = id;		
	}
	
	public int getFullHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setHeight(int newHeight) {
		// TODO Auto-generated method stub
		
	}

}
