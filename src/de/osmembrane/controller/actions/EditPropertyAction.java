package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;

public class EditPropertyAction extends AbstractAction {

	public EditPropertyAction() {
		putValue(Action.NAME, "Edit Property");
		putValue(Action.SMALL_ICON, new IconLoader("property_edit.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("property_edit.png",
				Size.NORMAL).get());
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}