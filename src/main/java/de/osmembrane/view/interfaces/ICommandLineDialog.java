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

package de.osmembrane.view.interfaces;

import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.view.dialogs.CommandLineDialog;

/**
 * Interface for {@link CommandLineDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface ICommandLineDialog extends IView {

    /**
     * Sets the pipeline to process.
     * 
     * @param pipeline
     *            the pipeline;
     */
    public void setPipeline(AbstractPipeline pipeline);

}
