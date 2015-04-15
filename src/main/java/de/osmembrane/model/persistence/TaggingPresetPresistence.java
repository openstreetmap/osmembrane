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

import java.net.URL;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.openstreetmap.josm.tagging_preset_1.Root;
import de.osmembrane.model.persistence.FileException.Type;

/**
 * Loads the osmosis structure from a given file and returns a
 * 
 * @author jakob_jarosch
 */
public class TaggingPresetPresistence extends AbstractPersistence {

    @Override
    public Object load(URL file) throws FileException {
        JAXBContext jc;
        try {
            jc = JAXBContext
                    .newInstance("de.openstreetmap.josm.tagging_preset_1");

            if (file == null) {
                throw new FileException(Type.NOT_FOUND);
            }

            Unmarshaller u = jc.createUnmarshaller();

            @SuppressWarnings("unchecked")
            JAXBElement<Root> root = (JAXBElement<Root>) u.unmarshal(file);

            return root.getValue();
        } catch (JAXBException e) {
            throw new FileException(Type.WRONG_FORMAT, e);
        }
    }

    @Deprecated
    @Override
    public void save(URL file, Object data) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void update(Observable o, Object arg) {
        return;
    }
}
