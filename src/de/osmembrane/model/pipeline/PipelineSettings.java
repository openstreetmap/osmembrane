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

/**
 * Implementation of {@link AbstractPipelineSettings}.
 * 
 * @author jakob_jarosch
 */
public class PipelineSettings extends AbstractPipelineSettings {

    private static final long serialVersionUID = 2011021314390001L;

    private int verboseMode = -1;
    private int debugMode = -1;
    private String comment = new String();
    private String name = new String();

    @Override
    public void setVerbose(int mode) {
        this.verboseMode = mode;
    }

    @Override
    public int getVerbose() {
        return verboseMode;
    }

    @Override
    public void setDebug(int mode) {
        this.debugMode = mode;
    }

    @Override
    public int getDebug() {
        return debugMode;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
