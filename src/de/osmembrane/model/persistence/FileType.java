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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import de.osmembrane.model.parser.BashParser;
import de.osmembrane.model.parser.CmdParser;
import de.osmembrane.model.parser.ExecutionParser;
import de.osmembrane.model.parser.IParser;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.tools.I18N;

/**
 * Represents the different FileTypes.
 * 
 * @author jakob_jarosch
 */
public enum FileType {

    /**
     * Bash normally used under UNIX systems.
     */
    BASH(new String[] { ".sh" }, BashPersistence.class, BashParser.class),

    /**
     * CMD normally used under Windows systems.
     */
    CMD(new String[] { ".bat", ".cmd" }, CmdPersistence.class, CmdParser.class),

    /**
     * OSMembrane filetype.
     */
    OSMEMBRANE(new String[] { ".osmembrane" }, OSMembranePersistence.class,
            null),

    /**
     * All filetypes together.
     */
    ALLTYPES(new String[] { ".osmembrane", ".bat", ".cmd", ".sh" }, null, null),

    /**
     * Only used to generate a pipeline compatible with the execution library.
     */
    EXECUTION_FILETYPE(null, null, ExecutionParser.class);

    private static final FileType[] autoselectableFileTypes = { BASH, CMD,
            OSMEMBRANE };

    /**
     * {@link FileType} as a string.
     */
    private String[] extensions;

    /**
     * Matching persistence for the {@link FileType}.
     */
    private Class<? extends AbstractPersistence> persistenceClass;

    /**
     * Matching parser for the {@link FileType}.
     */
    private Class<? extends IParser> parserClass;

    private FileType(String[] extensions,
            Class<? extends AbstractPersistence> persistenceClass,
            Class<? extends IParser> parserClass) {
        this.extensions = extensions;
        this.persistenceClass = persistenceClass;
        this.parserClass = parserClass;
    }

    /**
     * Returns the default extension as a String.
     * 
     * @return extension as a String
     */
    public String getExtension() {
        return extensions[0];
    }

    /**
     * Returns all possible extensions for a filetype as an array of strings.
     * 
     * @return all possible extensions for a filetype as an array of strings.
     */
    public String[] getAllExtensions() {
        return extensions;
    }

    /**
     * Returns the FileFilter for the filetype.
     * 
     * @return FileFilter of the filetype.
     */
    public FileFilter getFileFilter() {
        return new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (getAllExtensions() == null) {
                    return false;
                }

                for (String extension : getAllExtensions()) {
                    if (f.getName().toLowerCase().endsWith(extension)
                            || f.isDirectory()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < getAllExtensions().length; i++) {
                    builder.append("*");
                    builder.append(getAllExtensions()[i]);
                    if (i + 1 < getAllExtensions().length) {
                        builder.append(", ");
                    }
                }

                return getName() + " (" + builder.toString() + ")";
            }
        };
    }

    /**
     * Returns the matching {@link AbstractPersistence} class for load and save.
     * 
     * @return matching {@link AbstractPersistence} class.
     */
    public Class<? extends AbstractPersistence> getPersistenceClass() {
        return persistenceClass;
    }

    /**
     * Returns the matching {@link IParser} class for parsing a
     * {@link AbstractPipeline}.
     * 
     * @return matching {@link IParser} class
     */
    public Class<? extends IParser> getParserClass() {
        return parserClass;
    }

    /**
     * Returns the internationalized name as String.
     * 
     * @return the internationalized name as String
     */
    public String getName() {
        return I18N.getInstance().getString(
                "Controller.Actions.FileType." + this.toString() + ".Name");
    }

    /**
     * Returns the a corresponding filetype for a given filename.
     * 
     * @param file
     *            filename for which the filetype is needed.
     * 
     * @return filetype if a matching one is found, otherwise NULL
     */
    public static FileType fileTypeFor(File file) {
        for (FileType fileType : autoselectableFileTypes) {
            if (fileType.getFileFilter().accept((file)) && fileType != ALLTYPES) {
                return fileType;
            }
        }
        return null;
    }
}
