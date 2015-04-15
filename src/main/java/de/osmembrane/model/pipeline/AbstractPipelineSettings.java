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

package de.osmembrane.model.pipeline;

import java.io.Serializable;

/**
 * Represents the Settings of a pipeline.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractPipelineSettings implements Serializable {

    private static final long serialVersionUID = 2011021314390001L;

    /**
     * Sets the verbose level of the pipeline, used for osmosis execution.
     * 
     * @param mode
     *            0 is default, -1 disables the verbose parameter.
     */
    public abstract void setVerbose(int mode);

    /**
     * @return the verbose level for osmosis execution
     */
    public abstract int getVerbose();

    /**
     * Sets the debug level of the pipeline, used for osmosis execution.
     * 
     * @param mode
     *            0 is default, -1 disables the debug parameter.
     */
    public abstract void setDebug(int mode);

    /**
     * @return the debug level for osmosis execution
     */
    public abstract int getDebug();

    /**
     * Sets a comment for the pipeline.
     * 
     * @param comment
     *            which should be set
     */
    public abstract void setComment(String comment);

    /**
     * @return the comment of the pipeline
     */
    public abstract String getComment();

    /**
     * Sets a name for the pipeline.
     * 
     * @param name
     *            which should b e set
     */
    public abstract void setName(String name);

    /**
     * @return the name of the pipeline
     */
    public abstract String getName();
}
