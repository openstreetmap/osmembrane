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

package de.osmembrane.model.persistence;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Semaphore;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.PipelineObserverObject;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;

/**
 * Pipeline backup service on a separate background thread;
 * 
 * @author igor_podolskiy, jakob_jarosch
 * 
 */
public class PipelineBackup implements Observer {

    private Semaphore backupAvailable = new Semaphore(0);
    private AbstractPipeline pipelineToBackup;

    /**
     * Internal autosave thread used for backing up the pipeline.
     */
    private Thread autosaveThread = new Thread() {

        /* anonymous class' constructor */
        {
            setDaemon(true);
            setName("OSMembrane Backup Thread");
        }

        @Override
        public void run() {

            while (!isInterrupted()) {
                try {
                    backupAvailable.acquire();
                    backupAvailable.drainPermits();
                    pipelineToBackup.backupPipeline();
                } catch (InterruptedException e) {
                    interrupt();
                } catch (FileException e) {
                    /* forward the exception to the view */
                    Application.handleException(new ControlledException(this,
                            ExceptionSeverity.WARNING, e, I18N.getInstance()
                                    .getString(
                                            "Exception.AutosavePipelineFailed",
                                            Constants.DEFAULT_BACKUP_FILE)));

                }
            }
        }
    };

    @Override
    public synchronized void update(Observable o, Object arg) {
        if (arg instanceof PipelineObserverObject) {
            this.pipelineToBackup = ((PipelineObserverObject) arg)
                    .getPipeline();
            this.backupAvailable.release();
        }
    }

    /**
     * Starts the background backup handler.
     */
    public void start() {
        autosaveThread.start();
    }

    /**
     * Stops the background backup handler.
     */
    public void stop() {
        autosaveThread.interrupt();
    }
}
