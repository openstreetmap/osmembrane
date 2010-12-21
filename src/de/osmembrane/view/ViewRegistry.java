package de.osmembrane.view;

import java.util.ArrayList;
import de.osmembrane.view.IView;

public class ViewRegistry {
	private ViewRegistry instance;
	private Map<Class, IView> views;
	public ArrayList<IView> unnamed_IView_ = new ArrayList<IView>();

	public ViewRegistry getInstance() {
		throw new UnsupportedOperationException();
	}

	public void register(IView view) {
		throw new UnsupportedOperationException();
	}

	public void get(Class clazz) {
		throw new UnsupportedOperationException();
	}
}