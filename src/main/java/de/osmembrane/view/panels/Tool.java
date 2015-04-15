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

package de.osmembrane.view.panels;

/**
 * Represents the tool currently in use on the {@link PipelinePanel}.
 * 
 * @see "Spezifikation.pdf, chapter 2.1.3 (German)"
 * 
 * @author tobias_kuhn
 * 
 */
public enum Tool {
    /**
     * The magic default tool, that behaves as follows:
     * <ul>
     * <li>if a {@link PipelineFunction} or {@link PipelineLink} is below the
     * cursor, it behaves like the selection tool</li>
     * <li>if nothing is below the cursor, it behaves like the view tool</li>
     * <li>if a {@link PipelineConnector} is below the cursor, it behaves like
     * the connection tool</li>
     * </ul>
     */
    DEFAULT_MAGIC_TOOL,

    /**
     * tool to select {@link PipelineFunction}s or {@link PipelineLink}s in
     * order to move {@link PipelineFunction}s or delete selections
     */
    SELECTION_TOOL,

    /**
     * tool to move or zoom the {@link PipelinePanel}
     */
    VIEW_TOOL,

    /**
     * tool to create {@link PipelineLink}s
     */
    CONNECTION_TOOL;
}
