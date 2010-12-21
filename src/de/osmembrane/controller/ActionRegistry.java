package de.osmembrane.controller;

import java.util.ArrayList;
import javax.swing.Action;

public class ActionRegistry {
	private ActionRegistry instance;
	public ArrayList<Action> unnamed_Action_ = new ArrayList<Action>();

	public void getInstance() {
		throw new UnsupportedOperationException();
	}

	public void register(Action a) {
		throw new UnsupportedOperationException();
	}

	public void get(Class clazz) {
		throw new UnsupportedOperationException();
	}
}