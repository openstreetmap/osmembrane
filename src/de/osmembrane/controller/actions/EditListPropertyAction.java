package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.IListDialog;
import de.osmembrane.view.dialogs.ListDialog;

/**
 * Action to edit a parameter which is a list and therefore open the
 * {@link ListDialog}. Receives a {@link ContainingEvent}.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditListPropertyAction extends AbstractAction {

	private static final long serialVersionUID = 3473005271005241300L;

	/**
	 * Creates a new {@link EditListPropertyAction}
	 */
	public EditListPropertyAction() {
		putValue(Action.NAME, "Edit List Property");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"list_property_edit.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"list_property_edit.png", Size.NORMAL));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ContainingEvent ce = (ContainingEvent) e;
		AbstractParameter ap = (AbstractParameter) ce.getContained();

		IListDialog list = ViewRegistry.getInstance().getCasted(
				ListDialog.class, IListDialog.class);

		list.open(ap);
		if (list.shallApplyChanges()) {
			ap.setValue(list.getEdits());
		}

	}
}