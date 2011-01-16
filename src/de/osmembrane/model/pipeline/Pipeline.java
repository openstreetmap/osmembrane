package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.Stack;

import de.osmembrane.model.LoopCheck;
import de.osmembrane.model.parser.ParserFactory;
import de.osmembrane.model.persistence.AbstractPersistence;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.persistence.OSMembranePersistence;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.pipeline.PipelineObserverObject.ChangeType;

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

		/* register the Observer of Persistence to the Pipeline */
		addObserver(PersistenceFactory.getInstance());
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
		func.addObserver(this);

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(ChangeType.ADD, func));
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
			changedNotifyObservers(new PipelineObserverObject(ChangeType.ADD,
					func));
		}
		return returnValue;
	}

	@Override
	public void truncate() {
		this.functions.clear();

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null));
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
		for (AbstractFunction function : functions) {
			function.addObserver(function);
		}

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null));
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
		for (AbstractFunction function : functions) {
			function.addObserver(function);
		}

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null));
	}

	@Override
	public void exportPipeline(String filename, FileType type)
			throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(type.getPersistenceClass());

		persistence.save(filename, functions);
	}

	@Override
	public String generate(FileType filetype) {
		return ParserFactory.getInstance().getParser(filetype.getParserClass()).parsePipeline(functions);
	}

	@Override
	public boolean hasLoop() {
		LoopCheck check = new LoopCheck(functions);
		return check.hasLoop();
	}

	@Override
	public void optimizePipeline() {
		/* TODO not yet implemented */
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
	public void update(Observable o, Object arg) {

		if (arg instanceof PipelineObserverObject) {
			((PipelineObserverObject) arg).setPipeline(this);

			setChanged();
			notifyObservers(arg);
		}
	}
}