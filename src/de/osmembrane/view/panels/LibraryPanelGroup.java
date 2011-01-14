package de.osmembrane.view.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

/**
 * A group panel that is placed for each FunctionGroup on the LibraryPanel
 * 
 * @author tobias_kuhn
 * 
 */
public class LibraryPanelGroup extends JPanel {

	private static final long serialVersionUID = -2502154263887966328L;

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
	 * The height of the contained objects
	 */
	private int contentHeight;
	
	/**
	 * The function group represented
	 */
	private AbstractFunctionGroup functionGroup;

	/**
	 * The contained objects
	 */
	private List<ViewFunction> content;

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
		this.functionGroup = afg;
		
		// display
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		// best decision ever <- do not touch
		setLayout(null);

		int y = 3;
		// find the preferred width by using the maximum of all child objects
		int maxPreferredWidth = 0;

		// header button
		headerButton = new JButton();
		headerButton.setText(afg.getFriendlyName());

		// determine size, etc.
		headerButton.setLocation(3, y);
		headerButton.setSize(headerButton.getPreferredSize());
		maxPreferredWidth = headerButton.getPreferredSize().width;
		y += headerButton.getHeight() + 6;

		// action listener
		headerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lp.groupClicked(id);
			}
		});
		
		// hint listener for function group
		headerButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// show no hint
				IView mainFrame = ViewRegistry.getInstance().getMainFrame();
				MainFrame mf = (MainFrame) mainFrame;
				mf.getPipeline().setHint("");
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// show hint for this function group
				IView mainFrame = ViewRegistry.getInstance().getMainFrame();
				MainFrame mf = (MainFrame) mainFrame;
				mf.getPipeline().setHint(functionGroup.getDescription());
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		add(headerButton);

		content = new ArrayList<ViewFunction>();
		// all functions available in the function group
		for (AbstractFunction af : afg.getFunctions()) {
			ViewFunction vf = new ViewFunction(af);

			// determine the top
			vf.setLocation(3, y);
			// display them fully
			vf.setSize(vf.getPreferredSize());
			// find the maximum necessary width
			maxPreferredWidth = Math.max(maxPreferredWidth,
					vf.getPreferredSize().width);
			y += vf.getHeight() + 6;

			contentHeight += vf.getPreferredSize().height + 7;

			content.add(vf);
			add(vf);
		}

		setPreferredSize(new Dimension(maxPreferredWidth, 0));
	}

	/**
	 * @param id
	 *            the id this panel group has in its main Library panel to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the height of this group's contents, if they are expanded
	 */
	public int getFullContentHeight() {
		return contentHeight;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension result = super.getPreferredSize();
		result.height = headerButton.getPreferredSize().height;
		return result;
	}

	/**
	 * Sets the height for this group's contents
	 * 
	 * @param newHeight
	 *            0, if contracted, getFullHeight() if expanded
	 */
	public void setContentHeight(int newHeight) {
		setSize(getWidth(), headerButton.getPreferredSize().height + 6
				+ newHeight);
	}

	/**
	 * Gets called when the library panel has rearranged the library panel group
	 */
	public void rearranged() {
		// correct the header button's width
		headerButton.setSize(getWidth() - 6,
				headerButton.getPreferredSize().height);
		// center all the content view functions
		for (ViewFunction vf : content) {
			vf.setLocation((getWidth() - vf.getWidth()) / 2, vf.getLocation().y);
		}
	}

}
