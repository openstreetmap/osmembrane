package de.osmembrane.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.osmembrane.model.persistence.AbstractPersistence;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.persistence.OSMembranePersistence;
import de.osmembrane.model.persistence.PersistenceFactory;

/**
 * Implementation of {@link AbstractPipeline}.
 * 
 * @author jakob_jarosch
 */
public class Pipeline extends AbstractPipeline {

	@SuppressWarnings("unused")
	private Stack<Pipeline> undoStack;

	@SuppressWarnings("unused")
	private Stack<Pipeline> redoStack;

	private List<AbstractFunction> functions;

	/**
	 * Constructor for {@link Pipeline}.
	 */
	public Pipeline() {
		this.functions = new ArrayList<AbstractFunction>();
	}
	
	@Override
	public AbstractFunction[] getFunctions() {
		AbstractFunction[] functions = new AbstractFunction[this.functions
				.size()];
		functions = this.functions.toArray(functions);
		return functions;
	}

	@Override
	public void addFunction(AbstractFunction func) {
		func.setPipeline(this);
		functions.add(func);

		/* notify the observers */
		changedNotifyObservers();
	}

	@Override
	public boolean deleteFunction(AbstractFunction func) {
		boolean returnValue = false;

		for (AbstractFunction function : functions) {
			if (function == func) {
				returnValue = functions.remove(function);
			}
		}

		if (returnValue == true) {
			/* notify the observers */
			changedNotifyObservers();
		}
		return returnValue;
	}

	@Override
	public void truncate() {
		this.functions.clear();

		/* notify the observers */
		changedNotifyObservers();
	}

	@Override
	public void savePipeline(String filename) throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(OSMembranePersistence.class);

		persistence.save(filename, functions);
	}

	@Override
	public void loadPipeline(String filename) throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(OSMembranePersistence.class);

		Object obj = persistence.load(filename);

		/* is checked by persistence */
		@SuppressWarnings("unchecked")
		List<AbstractFunction> functions = (List<AbstractFunction>) obj;

		this.functions = functions;

		/* notify the observers */
		changedNotifyObservers();
	}

	@Override
	public void importPipeline(String filename, FileType type)
			throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(type.getPersistenceClass());

		Object obj = persistence.load(filename);

		/* is checked by persistence */
		@SuppressWarnings("unchecked")
		List<AbstractFunction> functions = (List<AbstractFunction>) obj;

		this.functions = functions;

		/* notify the observers */
		changedNotifyObservers();
	}

	@Override
	public void exportPipeline(String filename, FileType type)
			throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(type.getPersistenceClass());

		persistence.save(filename, functions);
	}

	@Override
	public String generate(String filetype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean undo() {
		/* TODO not yet implemented */
		return false;
	}

	@Override
	public boolean undoAvailable() {
		/* TODO not yet implemented */
		return false;
	}

	@Override
	public boolean redo() {
		/* TODO not yet implemented */
		return false;
	}

	@Override
	public boolean redoAvailable() {
		/* TODO not yet implemented */
		return false;
	}

	@Override
	public boolean checkForLoops() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void optimizePipeline() {
		/* TODO not yet implemented */
	}
}