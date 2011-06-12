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

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;

/**
 * Pipeline representation.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractPipeline extends Observable implements Observer {

    /**
     * Notice: The {@link AbstractPipelineSettings} are not observed by the
     * pipeline, so any changes made in the {@link AbstractPipelineSettings}
     * will <b>not</b> result in a observer message by the pipeline.
     * 
     * @return the settings of the pipeline.
     */
    public abstract AbstractPipelineSettings getSettings();

    /**
     * Creates an empty pipeline.
     */
    public abstract void clear();

    /**
     * Returns the {@link AbstractFunction}s in the pipeline.
     * 
     * @return array of {@link AbstractFunction}s
     */
    public abstract AbstractFunction[] getFunctions();

    /**
     * Adds a {@link AbstractFunction} to the pipeline.
     * 
     * @param func
     *            function which should be added
     */
    public abstract void addFunction(AbstractFunction func);

    /**
     * Removes a {@link AbstractFunction} from the pipeline.
     * 
     * @param func
     *            function which should be removed
     */
    public abstract boolean deleteFunction(AbstractFunction func);

    /**
     * Returns if the pipeline is complete or not. Complete means, that every
     * function in the pipeline is complete.
     * 
     * @return true if pipeline is complete, otherwise false
     */
    public abstract boolean isComplete();

    /**
     * Makes the last change undone.
     * 
     * @return false if the undo could not be done, otherwise true
     */
    public abstract boolean undo();

    /**
     * Returns the state of the undo-method.
     * 
     * @return true if a undo is available, otherwise false
     */
    public abstract boolean undoAvailable();

    /**
     * Redos some made undo changes.
     * 
     * @return false if the redo could not be done, otherwise true
     */
    public abstract boolean redo();

    /**
     * Returns the state of the redo-method.
     * 
     * @return true if a redo is available, otherwise false
     */
    public abstract boolean redoAvailable();

    /**
     * Optimizes the pipeline. Only functions are rearranged.
     */
    public abstract void arrangePipeline();

    /**
     * Loads a pipeline from a OSMembrane file.
     * 
     * @param filename
     *            path to the OSMembrane file
     * 
     * @throws FileException
     *             when something with the IO went wrong
     */
    public abstract void loadPipeline(URL filename) throws FileException;

    /**
     * Saves the pipeline to a OSMembrane file, only callable if a pipeline was
     * loaded, or it has already been saved.
     * 
     * @throws FileException
     *             when something with the IO went wrong
     */
    public abstract void savePipeline() throws FileException;

    /**
     * Saves the pipeline to a OSMembrane file.
     * 
     * @param filename
     *            path to the OSMembrane file
     * 
     * @throws FileException
     *             when something with the IO went wrong
     */
    public abstract void savePipeline(URL filename) throws FileException;

    /**
     * Creates a String-representation for the current pipeline.
     * 
     * @param filetype
     *            should be created
     */
    public abstract String generate(FileType filetype);

    /**
     * Imports the pipeline from a given file.
     * 
     * @param filename
     *            path to the file which should be loaded
     * @param type
     *            {@link FileType} of the given file
     * 
     * @throws FileException
     *             when something with the IO went wrong
     */
    public abstract void importPipeline(URL filename, FileType type)
            throws FileException;

    /**
     * Exports the pipeline a file on the given path.
     * 
     * @param filename
     *            path where the file should be saved
     * @param type
     *            {@link FileType} of the given file
     * 
     * @throws FileException
     *             when something with the IO went wrong
     */
    public abstract void exportPipeline(URL filename, FileType type)
            throws FileException;

    /**
     * Saves the pipeline to the default backup file.
     * 
     * @throws FileException
     *             when something with the IO went wrong
     */
    public abstract void backupPipeline() throws FileException;

    /**
     * Returns if a backup is available or not.
     * 
     * @return true if a backup is available, otherwise false
     */
    public abstract boolean isBackupAvailable();

    /**
     * Loads the backup into the pipeline.
     * 
     * @throws FileException
     *             when something with the IO went wrong
     */
    public abstract void loadBackup() throws FileException;

    /**
     * Deletes the local backup file.
     */
    public abstract void clearBackup();

    /**
     * Checks is the pipeline contains any loops, what is not right.
     * 
     * @return true if pipeline contains a loop, otherwise false
     */
    public abstract boolean hasLoop();

    /**
     * Returns the save-state of the pipeline.
     * 
     * @return true when pipeline is saved
     */
    public abstract boolean isSaved();

    /**
     * Returns the filename of the current pipeline.
     * 
     * @return filename of the pipeline or NULL if pipeline was not saved yet
     */
    public abstract URL getFilename();

    /**
     * Notifies all registered {@link Observer}s with pre-called
     * {@link Observable#setChanged()}.
     */
    protected abstract void changedNotifyObservers(PipelineObserverObject poo);
}
