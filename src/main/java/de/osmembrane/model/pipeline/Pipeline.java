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

package de.osmembrane.model.pipeline;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.algorithms.GraphPlanarizer;
import de.osmembrane.model.algorithms.TarjanAlgorithm;
import de.osmembrane.model.parser.ParserFactory;
import de.osmembrane.model.persistence.AbstractPersistence;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.persistence.OSMembranePersistence;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.persistence.PipelinePersistenceObject;
import de.osmembrane.model.pipeline.PipelineObserverObject.ChangeType;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.Tools;

/**
 * Implementation of {@link AbstractPipeline}.
 * 
 * @author jakob_jarosch
 */
public class Pipeline extends AbstractPipeline {

    private Stack<PipelineMemento> undoStack;
    private PipelineMemento currentState;
    private Stack<PipelineMemento> redoStack;

    private List<AbstractFunction> functions;

    private URL pipelineFilename;

    private AbstractPipelineSettings pipelineSettings;

    private boolean savedState;

    /**
     * Says if the pipeline is silent or not.<br/>
     * In the silent-mode the pipeline will not inform any observers.
     */
    private boolean silent;

    /**
     * Says if the pipeline uses undo-redo or not.<br/>
     * in disabled mode the pipeline does not save any undo redo steps.
     */
    private boolean undoRedoDisabled;

    /**
     * Creates a default pipeline with<br/>
     * 
     * @link {@link Pipeline#silent} = false
     */
    public Pipeline() {
        this(false);
    }

    /**
     * Creates a default pipeline with<br/>
     * 
     * @link {@link Pipeline#undoRedoDisabled} = false
     */
    public Pipeline(boolean silent) {
        this(silent, false);
    }

    /**
     * Constructor for {@link Pipeline}.
     */
    public Pipeline(boolean silent, boolean undoRedoDisabled) {
        this.functions = new ArrayList<AbstractFunction>();
        this.undoStack = new Stack<PipelineMemento>();
        this.redoStack = new Stack<PipelineMemento>();
        this.silent = silent;
        this.undoRedoDisabled = undoRedoDisabled;
        this.savedState = true;
        this.pipelineFilename = null;
        this.pipelineSettings = new PipelineSettings();

        /* register the Observer of Persistence to the Pipeline */
        addObserver(PersistenceFactory.getInstance());
    }

    @Override
    public AbstractPipelineSettings getSettings() {
        return pipelineSettings;
    }

    @Override
    public void clear() {
        this.functions.clear();
        this.undoStack.clear();
        this.redoStack.clear();
        pipelineSettings = new PipelineSettings();
        pipelineFilename = null;

        changeSavedState(true);

        /* notify the observers */
        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.FULLCHANGE, null).setCreateUndoStep(false));
    }

    @Override
    public AbstractFunction[] getFunctions() {
        AbstractFunction[] functions = new AbstractFunction[this.functions
                .size()];
        functions = this.functions.toArray(functions);
        return functions;
    }

    @Override
    public void addFunction(AbstractFunction func) {
        func.setPipeline(this);
        func.addObserver(this);
        functions.add(func);

        /* notify the observers */
        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.ADD_FUNCTION, func));
    }

    @Override
    public boolean deleteFunction(AbstractFunction func) {
        boolean returnValue = false;

        for (AbstractFunction function : functions) {
            if (function == func) {
                function.unlinkConnectors();
                returnValue = functions.remove(function);
                break;
            }
        }

        if (returnValue == true) {
            /* notify the observers */
            changedNotifyObservers(new PipelineObserverObject(
                    ChangeType.DELETE_FUNCTION, func));
        }

        return returnValue;
    }

    @Override
    public boolean isComplete() {
        /* check all functions */
        for (AbstractFunction function : getFunctions()) {
            if (!function.isComplete()) {
                return false;
            }
        }

        /* all functions seems to be complete, so the pipeline is also complete. */
        return true;
    }

    @Override
    public void loadPipeline(URL filename) throws FileException {
        AbstractPersistence persistence = PersistenceFactory.getInstance()
                .getPersistence(OSMembranePersistence.class);

        PipelinePersistenceObject pipeline = (PipelinePersistenceObject) persistence
                .load(filename);

        clear();

        pipelineFilename = filename;
        this.functions = pipeline.getFunctions();
        for (AbstractFunction function : functions) {
            function.addObserver(this);
        }

        this.pipelineSettings = pipeline.getSettings();

        changeSavedState(true);

        /* notify the observers */
        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.FULLCHANGE, null).setCreateUndoStep(false));
    }

    @Override
    public void savePipeline() throws FileException {
        savePipeline(pipelineFilename);
    }

    @Override
    public void savePipeline(URL filename) throws FileException {
        AbstractPersistence persistence = PersistenceFactory.getInstance()
                .getPersistence(OSMembranePersistence.class);

        persistence.save(filename, new PipelinePersistenceObject(functions,
                pipelineSettings));
        pipelineFilename = filename;

        /* Saved successfully (persistence has not thrown a FileException */
        changeSavedState(true);

        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.SAVED_PIPELINE, null).setCreateUndoStep(false));
    }

    @Override
    public boolean isSaved() {
        return savedState;
    }

    @Override
    public URL getFilename() {
        return pipelineFilename;
    }

    @Override
    public void backupPipeline() throws FileException {
        AbstractPersistence persistence = PersistenceFactory.getInstance()
                .getPersistence(OSMembranePersistence.class);

        persistence.save(Constants.DEFAULT_BACKUP_FILE,
                new PipelinePersistenceObject(functions, pipelineSettings));
    }

    @Override
    public boolean isBackupAvailable() {
        File file = Tools.urlToFile(Constants.DEFAULT_BACKUP_FILE);
        return file.isFile();
    }

    @Override
    public void loadBackup() throws FileException {
        loadPipeline(Constants.DEFAULT_BACKUP_FILE);
    }

    @Override
    public void clearBackup() {
        if (isBackupAvailable()) {
            Tools.urlToFile(Constants.DEFAULT_BACKUP_FILE).delete();
        }
    }

    @Override
    public void importPipeline(URL filename, FileType type)
            throws FileException {
        AbstractPersistence persistence = PersistenceFactory.getInstance()
                .getPersistence(type.getPersistenceClass());

        PipelinePersistenceObject pipeline = (PipelinePersistenceObject) persistence
                .load(filename);

        clear();

        this.functions = pipeline.getFunctions();
        for (AbstractFunction function : functions) {
            function.addObserver(this);
        }

        this.pipelineSettings = pipeline.getSettings();

        /* notify the observers */
        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.FULLCHANGE, null));
    }

    @Override
    public String generate(FileType filetype) {
        return ParserFactory
                .getInstance()
                .getParser(filetype.getParserClass())
                .parsePipeline(
                        new PipelinePersistenceObject(functions,
                                pipelineSettings));
    }

    @Override
    public void exportPipeline(URL filename, FileType type)
            throws FileException {
        AbstractPersistence persistence = PersistenceFactory.getInstance()
                .getPersistence(type.getPersistenceClass());

        persistence.save(filename, new PipelinePersistenceObject(functions,
                pipelineSettings));
    }

    @Override
    public boolean hasLoop() {
        TarjanAlgorithm check = new TarjanAlgorithm(functions);
        check.run();

        /* check the SCCs */
        List<List<AbstractFunction>> sccs = check.getSCC();

        for (List<AbstractFunction> scc : sccs) {
            if (scc.size() == 1) {
                /*
                 * check if the scc with size 1 links to itself or is just
                 * standing alone
                 */
                AbstractFunction function = scc.get(0);
                for (AbstractConnector outConnector : function
                        .getOutConnectors()) {
                    for (AbstractConnector inConnector : outConnector
                            .getConnections()) {
                        if (inConnector.getParent() == function) {
                            /* found a connection to the function itself */
                            return true;
                        }
                    }
                }
            } else if (scc.size() > 1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void arrangePipeline() {
        GraphPlanarizer gprizer = new GraphPlanarizer(functions);
        gprizer.planarize();
        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.FULLCHANGE, null));
    }

    @Override
    public boolean undo() {
        if (!undoAvailable()) {
            return false;
        }

        redoStack.push(currentState);
        restoreMemento(undoStack.pop());

        return true;
    }

    @Override
    public boolean undoAvailable() {
        return !undoStack.isEmpty();
    }

    @Override
    public boolean redo() {
        if (!redoAvailable()) {
            return false;
        }

        undoStack.push(currentState);
        restoreMemento(redoStack.pop());

        return true;
    }

    @Override
    public boolean redoAvailable() {
        return !redoStack.isEmpty();
    }

    @Override
    public void update(Observable o, Object arg) {
        /* only PipelineObserverObjects will be passed through */
        if (arg instanceof PipelineObserverObject) {
            changedNotifyObservers((PipelineObserverObject) arg);
        }
    }

    @Override
    protected void changedNotifyObservers(PipelineObserverObject poo) {
        poo.setPipeline(this);

        /* check if the undo-step is really required, or disabled. */
        if (poo.createUndoStep() && !undoRedoDisabled) {
            /* any changes made, set savedState to false */
            changeSavedState(false);
        }

        if (!silent) {
            this.setChanged();
            this.notifyObservers(poo);
        }
    }

    private void changeSavedState(boolean state) {
        this.savedState = state;

        if (state == false) {
            saveStep();
        } else {
            /*
             * Update the savedState for the current item (nothing changed in
             * the pipeline only the state should be updated.
             */
            currentState = new PipelineMemento(functions, savedState);
        }
    }

    private void saveStep() {
        undoStack.push(currentState);
        redoStack.clear();
        currentState = new PipelineMemento(functions, savedState);

        int maximumStackSize = ((Integer) ModelProxy.getInstance()
                .getSettings().getValue(SettingType.MAXIMUM_UNDO_STEPS))
                .intValue();

        while (undoStack.size() > maximumStackSize) {
            undoStack.remove(0);
        }
    }

    private void restoreMemento(PipelineMemento memento) {
        this.functions = memento.getFunctions();
        this.savedState = memento.getSavedState();
        this.currentState = memento;

        for (AbstractFunction function : functions) {
            function.addObserver(this);
        }

        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.FULLCHANGE, null).setCreateUndoStep(false));
    }
}

/**
 * Private internal class of the memento of the undo-/redo-feature.
 * 
 * @author jakob_jarosch
 */
class PipelineMemento {

    private List<AbstractFunction> functions = new ArrayList<AbstractFunction>();
    private boolean savedState;

    public PipelineMemento(List<AbstractFunction> functions, boolean savedState) {
        this.functions = deepCopyFunctions(functions);
        this.savedState = savedState;
    }

    public boolean getSavedState() {
        return savedState;
    }

    public List<AbstractFunction> getFunctions() {
        return deepCopyFunctions(functions);
    }

    private List<AbstractFunction> deepCopyFunctions(
            List<AbstractFunction> functions) {
        /* Use serialization to create a copy of the functions in the pipeline */
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(functions);
            ByteArrayInputStream bais = new ByteArrayInputStream(
                    baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object deepCopy = ois.readObject();

            @SuppressWarnings("unchecked")
            List<AbstractFunction> copy = (List<AbstractFunction>) deepCopy;
            return copy;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return functions;
    }
}
