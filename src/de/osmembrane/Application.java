package de.osmembrane;

import java.util.Locale;

import javax.swing.JOptionPane;

import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.interfaces.IView;

/**
 * The one and only OO instance of the main program
 * 
 * @author tobias_kuhn
 * 
 */
public class Application {

	/**
	 * Connects the most basic stuff of the MVC architecture
	 */
	public void initiate() {
		try {
			// connect model and view
			ModelProxy.getInstance().addObserver(ViewRegistry.getInstance());

			// set the EDT Exception handler
			System.setProperty("sun.awt.exception.handler",
					EDTExceptionHandler.class.getName());

		} catch (Exception e) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e, I18N
							.getInstance().getString(
									"GenericInitializationCriticalError")));
		}
	}

	/**
	 * Initializes the model.
	 */
	public void createModels() {
		try {
			ModelProxy.getInstance().getFunctions()
					.initiate(Resource.OSMEMBRANE_XML.getURL());

			ModelProxy.getInstance().getPreset()
					.initiate(Resource.PRESET_XML.getURL());

			ModelProxy.getInstance().getSettings().initiate();
		} catch (Exception e) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e, I18N
							.getInstance().getString(
									"GenericInitializationCriticalError")));
		}
	}

	/**
	 * Sets the active locale.
	 */
	public void setLocale() {
		Locale activeLocale = (Locale) ModelProxy.getInstance().getSettings()
				.getValue(SettingType.ACTIVE_LANGUAGE);

		I18N.getInstance().setLocale(activeLocale);
	}

	/**
	 * Shows the main window after application startup. Is guaranteed to be
	 * invoked by a different {@link Runnable}
	 */
	public void showMainFrame() {
		try {
			IView mainFrame = ViewRegistry.getInstance().getMainFrame();
			mainFrame.showWindow();
			mainFrame.bringToFront();
		} catch (Exception e) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e, I18N
							.getInstance().getString(
									"GenericInitializationCriticalError")));
		}
	}

	/**
	 * Called whenever there's a {@link Throwable} to catch.
	 */
	public static void handleException(Throwable t) {
		// if it's one of our own, decode it
		if (t instanceof ControlledException) {
			ControlledException ce = (ControlledException) t;

			Throwable toShow = t;
			if ((ce.getCause() != null) && (ce.getMessage() == null)) {
				toShow = ce.getCause();
			}
			ViewRegistry.showException(toShow, ce.getKind(),
					ce.getCausingObject());
		} else {
			// else the view will handle it (e.g. finding out about error cause
			// etc.)
			ViewRegistry.showException(t, null, null);
		}
	}

	public void checkForBackup() {
		boolean backupAvailable = ModelProxy.getInstance().getPipeline()
				.isBackupAvailable();

		boolean skippedLoad = false;
		
		if (backupAvailable) {
			int result = JOptionPane.showConfirmDialog(null, I18N.getInstance()
					.getString("Application.BackupPipelineFound"),
					"Application.BackupPipelineFound.Title",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				skippedLoad = true;
			}

			try {
				ModelProxy.getInstance().getPipeline().loadBackup();
			} catch (FileException e) {
				Application.handleException(new ControlledException(this,
						ExceptionSeverity.WARNING, e, I18N.getInstance()
								.getString(
										"Controller.Actions.Load.Failed."
												+ e.getType())));
			}
		}
		
		if (skippedLoad || !backupAvailable) {
			ModelProxy.getInstance().getPipeline().clear();
		}
	}
}