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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Observable;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.parser.IParser;
import de.osmembrane.model.parser.ParseException;
import de.osmembrane.model.parser.ParserFactory;
import de.osmembrane.model.persistence.FileException.Type;
import de.osmembrane.tools.Tools;

/**
 * Writes and Reads Bash-Files (normally used on UNIX systems).
 * 
 * @author jakob_jarosch
 */
public class BashPersistence extends AbstractPersistence {

    private static final Class<? extends IParser> PARSER = FileType.BASH
            .getParserClass();

    @Override
    public void save(URL filename, Object data) throws FileException {
        if (!(data instanceof PipelinePersistenceObject)) {
            Application.handleException(new ControlledException(this,
                    ExceptionSeverity.UNEXPECTED_BEHAVIOR,
                    "BashPersistence#save() got a wrong"
                            + " object, object is the following instance:\n"
                            + data.getClass()));
        }

        try {
            File file = Tools.urlToFile(filename);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            String output = ParserFactory.getInstance().getParser(PARSER)
                    .parsePipeline((PipelinePersistenceObject) data);

            bw.write(output);
            bw.close();
            fw.close();

        } catch (IOException e) {
            throw new FileException(Type.NOT_WRITABLE, e);
        }
    }

    @Override
    public Object load(URL filename) throws FileException {
        IParser parser = ParserFactory.getInstance().getParser(PARSER);

        try {
            InputStreamReader isr = new InputStreamReader(filename.openStream());
            BufferedReader br = new BufferedReader(isr);

            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.append(line + parser.getBreaklineCommand());
            }
            br.close();
            isr.close();

            PipelinePersistenceObject functions = parser
                    .parseString(fileContent.toString());

            return functions;

        } catch (FileNotFoundException e) {
            throw new FileException(Type.NOT_FOUND, e);
        } catch (IOException e) {
            throw new FileException(Type.NOT_READABLE, e);
        } catch (ParseException e) {
            throw new FileException(Type.SYNTAX_PROBLEM, e);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        return;
    }
}
