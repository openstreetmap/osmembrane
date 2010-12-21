package de.osmembrane.view;

import java.util.Vector;
import de.osmembrane.view.IView;

public class ViewRegistry {
	private ViewRegistry _instance;
	private Map<Class, IView> _views;
	public Vector<IView> _unnamed_IView_ = new Vector<IView>();

	public ViewRegistry getInstance() {
		throw new UnsupportedOperationException();
	}

	public void register(IView aView) {
		throw new UnsupportedOperationException();
	}

	public void get(Class aClazz) {
		throw new UnsupportedOperationException();
	}
}