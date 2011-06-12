package de.osmembrane.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

/**
 * Simple test for {@link I18N}.
 * 
 * @author jakob_jarosch
 */
public class I18NTest {

    private boolean updated = false;

    /**
     * Test the geString() method.
     */
    @Test
    public void testGetString() {
        String testString;
        String testStringEn;

        Observer observer = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                updated = true;
            }
        };

        I18N.getInstance().setLocale(Locale.GERMAN);
        I18N.getInstance().addObserver(observer);

        testString = I18N.getInstance().getString("View.Quit");
        assertEquals("Beenden", testString);

        testStringEn = I18N.getInstance().getString("osmembrane");
        assertEquals("OSMembrane", testStringEn);

        I18N.getInstance().setLocale(Locale.ENGLISH);
        assertTrue("Observer not updated", updated);

        testString = I18N.getInstance().getString("View.Quit");
        assertEquals("Quit", testString);

        testString = I18N.getInstance().getString("View.ErrorDialog.In", "1",
                "2");
        assertEquals("1 in 2", testString);
    }

}
