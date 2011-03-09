package de.osmembrane.tools;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class HeadlessSafe {
	public static int getMenuShortcutKeyMask(Toolkit tk) {
		if (GraphicsEnvironment.isHeadless()) {
			return KeyEvent.CTRL_MASK;
		}
		
		return tk.getMenuShortcutKeyMask();
	}
	
	public static int getMenuShortcutKeyMask() {
		return getMenuShortcutKeyMask(Toolkit.getDefaultToolkit());
	}
}
