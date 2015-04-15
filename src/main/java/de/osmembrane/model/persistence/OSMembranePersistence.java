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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Observable;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.persistence.FileException.Type;
import de.osmembrane.tools.Tools;

/**
 * Saves the OSMembrane Pipeline into a file.
 * 
 * @author jakob_jarosch
 */
public class OSMembranePersistence extends AbstractPersistence {

    /**
     * Creates a new {@link OSMembranePersistence} object and starts the
     * internal thread.
     */
    public OSMembranePersistence() {
    }

    @Override
    public void save(URL file, Object data) throws FileException {
        if (!(data instanceof PipelinePersistenceObject)) {
            Application.handleException(new ControlledException(this,
                    ExceptionSeverity.UNEXPECTED_BEHAVIOR,
                    "OSMembranePersistence#save() got a wrong"
                            + " object, object is the following instance:\n"
                            + data.getClass()));
        }

        try {
            FileOutputStream fos = new FileOutputStream(Tools.urlToFile(file));
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(data);
            oos.close();
            bos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(Type.NOT_WRITABLE, e);
        }
    }

    @Override
    public Object load(URL file) throws FileException {
        try {
            BufferedInputStream bis = new BufferedInputStream(file.openStream());
            ObjectInputStream ois = new ObjectInputStream(bis);

            PipelinePersistenceObject object = (PipelinePersistenceObject) ois
                    .readObject();

            ois.close();
            bis.close();

            return object;
        } catch (FileNotFoundException e) {
            throw new FileException(Type.NOT_FOUND, e);
        } catch (IOException e) {
            throw new FileException(Type.NOT_READABLE, e);
        } catch (ClassNotFoundException e) {
            throw new FileException(Type.WRONG_FORMAT, e);
        } catch (ClassCastException e) {
            throw new FileException(Type.WRONG_FORMAT, e);
        }
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
    }
}
