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

package de.osmembrane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.SplashScreen;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Locale;

import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.PipelineBackup;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Constants;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.Tools;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.AboutDialog;
import de.osmembrane.view.dialogs.BoundingBoxDialog;
import de.osmembrane.view.dialogs.CommandLineDialog;
import de.osmembrane.view.dialogs.ExceptionDialog;
import de.osmembrane.view.dialogs.ExecutionStateDialog;
import de.osmembrane.view.dialogs.FunctionPresetDialog;
import de.osmembrane.view.dialogs.ListDialog;
import de.osmembrane.view.dialogs.PipelineSettingsDialog;
import de.osmembrane.view.dialogs.SettingsDialog;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.frames.TutorialFrame;
import de.osmembrane.view.interfaces.IView;

/**
 * The one and only OO instance of the main program
 * 
 * @author tobias_kuhn
 * 
 */
public class Application {

    private PipelineBackup pipelineBackup;

    /**
     * Creates a new Application and writes the build number on the splash.
     */
    public Application() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash != null) {
            Graphics2D g = (Graphics2D) splash.createGraphics();
            Dimension size = splash.getSize();
            g.setColor(new Color(255, 255, 255));
            g.drawRect(0, 0, size.width, size.height);
            g.drawString(String.format("%s (%s)", Constants.VERSION,
                    Constants.REVISION_ID), size.width - 170, size.height - 45);
            g.setPaintMode();
            splash.update();
        }
    }

    /**
     * Connects the most basic stuff of the MVC architecture
     */
    public void initiate() {
        try {
            // connect model and view
            ModelProxy.getInstance().addObserver(ViewRegistry.getInstance());

            // guarantee the View is initialized
            ViewRegistry.getInstance();

            // guarantee the Controller is initialized
            ActionRegistry.getInstance();

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
            ModelProxy.getInstance().getSettings().initiate();

            ModelProxy.getInstance().getFunctions()
                    .initiate(Resource.OSMEMBRANE_XML.getURL());

            ModelProxy.getInstance().getPreset()
                    .initiate(Resource.PRESET_XML.getURL());

        } catch (Exception e) {
            e.printStackTrace();
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
            IView mainFrame = ViewRegistry.getInstance().getMainFrame(true);
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
        if (GraphicsEnvironment.isHeadless()) {
            throw new RuntimeException(t);
        }

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

    /**
     * Checks if a backup is needed to be load.
     */
    public void checkForBackup() {
        boolean backupAvailable = ModelProxy.getInstance().getPipeline()
                .isBackupAvailable();

        boolean skippedLoad = false;

        if (backupAvailable) {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    I18N.getInstance().getString(
                            "Application.BackupPipelineFound"),
                    I18N.getInstance().getString(
                            "Application.BackupPipelineFound.Title"),
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) {
                skippedLoad = true;
            } else {
                /* load the pipeline */
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
        }

        if (skippedLoad || !backupAvailable) {
            ModelProxy.getInstance().getPipeline().clear();
        }
    }

    /**
     * Creates the home directory of OSMembrane if it does not exists.
     */
    public void createHomeDirectory() {
        File home = Tools.urlToFile(Constants.DEFAULT_USER_FOLDER);
        if (!home.isDirectory()) {
            if (!home.mkdir()) {
                Application.handleException(new ControlledException(this,
                        ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, I18N
                                .getInstance().getString(
                                        "Execption.HomeFolderCreationFailed",
                                        home)));
            }
        }
    }

    // /**
    // * Checks for updates.
    // */
    // public void checkForUpdates() {
    // new Thread() {
    // public void run() {
    // SettingsTypeUpdateInterval interval = (SettingsTypeUpdateInterval)
    // ModelProxy
    // .getInstance().getSettings()
    // .getValue(SettingType.UPDATE_INTERVAL);
    // long timeDiff = interval.getTimeDiff();
    // long lastUpdateLookup = (Long) ModelProxy.getInstance()
    // .getSettings().getValue(SettingType.LAST_UPDATE_LOOKUP);
    // long currentTime = TimeUnit.MILLISECONDS.toSeconds(System
    // .currentTimeMillis());
    // URL updateSite = Constants.UPDATE_WEBSITE;
    //
    // if (timeDiff > 0) {
    // if (lastUpdateLookup + timeDiff < currentTime) {
    // /* update */
    // try {
    // BufferedReader br = new BufferedReader(
    // new InputStreamReader(
    // updateSite.openStream()));
    //
    // int availableBuild = 0;
    // String downloadSite = "";
    // StringBuilder message = new StringBuilder();
    //
    // String line;
    // int lineId = 0;
    // while ((line = br.readLine()) != null) {
    // if (lineId == 0) {
    // try {
    // availableBuild = Integer.parseInt(line);
    // } catch (NumberFormatException e) {
    // /* invalid version */
    // availableBuild = 0;
    // }
    // } else if (lineId == 1) {
    // downloadSite = line;
    // } else {
    // message.append("\n" + line);
    // }
    // lineId++;
    // }
    // br.close();
    // } catch (IOException e) {
    // /* hidden, not so important... */
    // }
    //
    // try {
    // ModelProxy
    // .getInstance()
    // .getSettings()
    // .setValue(SettingType.LAST_UPDATE_LOOKUP,
    // currentTime);
    // } catch (UnparsableFormatException e) {
    // }
    // }
    // }
    // }
    // }.start();
    // }

    /**
     * Configures some bits and pieces of the UI that are per application. This
     * method should be called before any UI components are created.
     */
    public void configureUIDefaults() {

        // Update default on every look and feel change.
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!"lookAndFeel".equals(evt.getPropertyName())) {
                    return;
                }
                configureInputMaps();
            }
        });

        // Update defaults once now
        configureInputMaps();
    }

    private void configureInputMaps() {
        InputMap inputMap = (InputMap) UIManager.getDefaults().get(
                "Button.focusInputMap");

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                "pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                "released");
    }

    void initializeBackup() {
        pipelineBackup = new PipelineBackup();
        ModelProxy.getInstance().getPipeline().addObserver(pipelineBackup);
        pipelineBackup.start();
    }

    void createViews() {
        ViewRegistry vr = ViewRegistry.getInstance();
        MainFrame mf = new MainFrame();
        vr.register(mf);
        vr.register(new AboutDialog(mf));
        vr.register(new BoundingBoxDialog(mf));
        vr.register(new CommandLineDialog(mf));
        vr.register(new ExceptionDialog(mf));
        vr.register(new ExecutionStateDialog(mf));
        vr.register(new FunctionPresetDialog(mf));
        vr.register(new ListDialog(mf));
        vr.register(new PipelineSettingsDialog(mf));
        vr.register(new SettingsDialog(mf));
        vr.register(new TutorialFrame());
    }
}
