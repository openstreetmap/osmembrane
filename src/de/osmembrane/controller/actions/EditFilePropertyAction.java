package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to edit a parameter which is a file path and therefore open the file
 * path dialog. Receives a {@link ContainingEvent}.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditFilePropertyAction extends AbstractAction {

	private static final long serialVersionUID = 1481319711002406388L;

	/**
	 * Creates a new {@link EditFilePropertyAction}
	 */
	public EditFilePropertyAction() {
		putValue(Action.NAME, "Edit File Property");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("file_property_edit.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("file_property_edit.png", Size.NORMAL));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}