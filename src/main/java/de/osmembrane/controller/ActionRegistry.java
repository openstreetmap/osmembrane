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

package de.osmembrane.controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;

import de.osmembrane.controller.actions.AddConnectionAction;
import de.osmembrane.controller.actions.AddFunctionAction;
import de.osmembrane.controller.actions.ArrangePipelineAction;
import de.osmembrane.controller.actions.ChangePipelineSettingsAction;
import de.osmembrane.controller.actions.ChangeSettingsAction;
import de.osmembrane.controller.actions.DeleteSelectionAction;
import de.osmembrane.controller.actions.DuplicateFunctionAction;
import de.osmembrane.controller.actions.EditBoundingBoxPropertyAction;
import de.osmembrane.controller.actions.EditDirectoryPropertyAction;
import de.osmembrane.controller.actions.EditFilePropertyAction;
import de.osmembrane.controller.actions.EditListPropertyAction;
import de.osmembrane.controller.actions.EditPropertyAction;
import de.osmembrane.controller.actions.ExecutePipelineAction;
import de.osmembrane.controller.actions.ExitAction;
import de.osmembrane.controller.actions.ExportPipelineAction;
import de.osmembrane.controller.actions.GeneratePipelineAction;
import de.osmembrane.controller.actions.ImportPipelineAction;
import de.osmembrane.controller.actions.LoadFunctionPresetAction;
import de.osmembrane.controller.actions.LoadPipelineAction;
import de.osmembrane.controller.actions.MoveFunctionAction;
import de.osmembrane.controller.actions.NewPipelineAction;
import de.osmembrane.controller.actions.PreviewPipelineAction;
import de.osmembrane.controller.actions.RedoAction;
import de.osmembrane.controller.actions.ResetViewAction;
import de.osmembrane.controller.actions.SaveAsPipelineAction;
import de.osmembrane.controller.actions.SaveFunctionPresetAction;
import de.osmembrane.controller.actions.SavePipelineAction;
import de.osmembrane.controller.actions.ShowAboutAction;
import de.osmembrane.controller.actions.ShowHelpAction;
import de.osmembrane.controller.actions.ShowQuickstartAction;
import de.osmembrane.controller.actions.UndoAction;
import de.osmembrane.controller.actions.ViewAllAction;
import de.osmembrane.controller.actions.ZoomInAction;
import de.osmembrane.controller.actions.ZoomOutAction;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.interfaces.IView;

/**
 * The action registry implements the Broker pattern to organize the
 * {@link Action}s.
 * 
 * @author tobias_kuhn
 * 
 */
public class ActionRegistry implements Observer {
    /**
     * implements the Singleton pattern
     */
    private static ActionRegistry instance = new ActionRegistry();

    /**
     * internal storage of the actions, indexed by class
     */
    public Map<Class<? extends Action>, Action> actions = new HashMap<Class<? extends Action>, Action>();

    /**
     * initializes the {@link ActionRegistry} with all the actions this
     * controller component has
     */
    private ActionRegistry() {
        initialize();
        ModelProxy.getInstance().getPipeline().addObserver(this);
    }

    /**
     * Initializes the {@link ActionRegistry} by adding all actions to it.
     */
    private void initialize() {
        // pipeline actions
        register(new NewPipelineAction());
        register(new SavePipelineAction());
        register(new SaveAsPipelineAction());
        register(new LoadPipelineAction());
        register(new ExportPipelineAction());
        register(new ImportPipelineAction());
        register(new GeneratePipelineAction());
        register(new ExecutePipelineAction());
        register(new PreviewPipelineAction());
        register(new ArrangePipelineAction());
        register(new ChangePipelineSettingsAction());

        // function actions
        register(new AddFunctionAction());
        register(new MoveFunctionAction());
        register(new DuplicateFunctionAction());
        register(new DeleteSelectionAction());

        // connection actions
        register(new AddConnectionAction());

        // property actions
        register(new EditPropertyAction());
        register(new EditListPropertyAction());
        register(new EditFilePropertyAction());
        register(new EditDirectoryPropertyAction());
        register(new EditBoundingBoxPropertyAction());

        // undo, redo actions
        register(new UndoAction());
        register(new RedoAction());

        // function presets
        register(new LoadFunctionPresetAction());
        register(new SaveFunctionPresetAction());

        // view actions
        register(new ResetViewAction());
        register(new ViewAllAction());
        register(new ZoomInAction());
        register(new ZoomOutAction());

        // other actions
        register(new ShowHelpAction());
        register(new ShowQuickstartAction());
        register(new ShowAboutAction());
        register(new ChangeSettingsAction());
        register(new ExitAction());

        // set setEnabled() values
        update(null, null);
    }

    /**
     * 
     * @return the one and only instance of {@link ActionRegistry}
     */
    public static ActionRegistry getInstance() {
        return instance;
    }

    /**
     * Adds an {@link Action} to the registry
     * 
     * @param action
     *            Action to add
     */
    public void register(Action action) {
        actions.put(action.getClass(), action);
    }

    /**
     * Returns an {@link Action} from the registry
     * 
     * @param clazz
     *            desired class to return
     * @return the registered object for that class
     */
    public Action get(Class<? extends Action> clazz) {
        return actions.get(clazz);
    }

    /**
     * Reinitializes the {@link ActionRegistry} after a language change.
     */
    public void reinitialize() {
        actions.clear();
        System.gc();
        initialize();
    }

    @Override
    public void update(Observable o, Object arg) {
        // update all actions and their enabled state

        boolean pipelineFull = (ModelProxy.getInstance().getPipeline()
                .getFunctions().length > 0);
        boolean isSaved = ModelProxy.getInstance().getPipeline().isSaved();

        get(UndoAction.class).setEnabled(
                ModelProxy.getInstance().getPipeline().undoAvailable());
        get(RedoAction.class).setEnabled(
                ModelProxy.getInstance().getPipeline().redoAvailable());
        get(SaveAsPipelineAction.class).setEnabled(pipelineFull);
        get(SavePipelineAction.class).setEnabled(!isSaved && pipelineFull);
        get(ArrangePipelineAction.class).setEnabled(pipelineFull);
        get(ExecutePipelineAction.class).setEnabled(pipelineFull);
        get(ExportPipelineAction.class).setEnabled(pipelineFull);
        get(PreviewPipelineAction.class).setEnabled(pipelineFull);
        get(GeneratePipelineAction.class).setEnabled(pipelineFull);

        URL url = ModelProxy.getInstance().getPipeline().getFilename();
        String fileName;

        // if not call from initializer
        if (o != null) {

            if (url == null) {
                fileName = I18N.getInstance().getString(
                        "Controller.UnsavedTitle");
            } else {
                fileName = new File(url.getPath()).getName().replaceAll(
                        "(?i)" + FileType.OSMEMBRANE.getExtension(), "");
            }

            // prevent quick creation, if this is a backup loading call
            IView mainFrame = ViewRegistry.getInstance().getMainFrame(false);
            if (mainFrame != null) {
                mainFrame.setWindowTitle(fileName + (isSaved ? "" : "*"));
            }
        }
    }
}
