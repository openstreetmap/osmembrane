package de.osmembrane.tools;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

/**
 * A helper class that allows asking for the menu shortcut key mask in a
 * headless environment. This class is mostly needed for execution of automated
 * tests.
 * 
 * @author Igor Podolskiy
 */
public class HeadlessSafe {
    /**
     * Returns the default shortcut key mask of the given toolkit, or CTRL_MASK
     * in a headless environment.
     * 
     * @param tk
     *            the toolkit to get the shortcut key mask for.
     * @return the shortcut key mask.
     */
    public static int getMenuShortcutKeyMask(Toolkit tk) {
        if (GraphicsEnvironment.isHeadless()) {
            return KeyEvent.CTRL_MASK;
        }

        return tk.getMenuShortcutKeyMask();
    }

    /**
     * Get the shortcut key mask for the default toolkit.
     * 
     * @return
     */
    public static int getMenuShortcutKeyMask() {
        return getMenuShortcutKeyMask(Toolkit.getDefaultToolkit());
    }
}
