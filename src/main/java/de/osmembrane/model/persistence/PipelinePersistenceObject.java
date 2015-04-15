/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL: https://osmembrane.de/svn/sources/src/header.txt $ ($Revision: 703 $)
 * Last changed: $Date: 2011-02-07 10:56:49 +0100 (Mo, 07 Feb 2011) $
 */

package de.osmembrane.model.persistence;

import java.io.Serializable;
import java.util.List;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractPipelineSettings;

/**
 * Represents a storable object for a pipeline.
 * 
 * @author jakob_jarosch
 */
public class PipelinePersistenceObject implements Serializable {

    private static final long serialVersionUID = 2011021314540001L;

    private List<AbstractFunction> functions;
    private AbstractPipelineSettings settings;

    /**
     * Creates a new {@link PipelinePersistenceObject}.
     * 
     * @param functions
     *            functions which should be saved
     * @param settings
     *            settings which should be saved
     */
    public PipelinePersistenceObject(List<AbstractFunction> functions,
            AbstractPipelineSettings settings) {
        this.settings = settings;
        this.functions = functions;
    }

    /**
     * @return the functions of the pipeline.
     */
    public List<AbstractFunction> getFunctions() {
        return functions;
    }

    /**
     * @return the settings of the pipeline.
     */
    public AbstractPipelineSettings getSettings() {
        return settings;
    }

}
