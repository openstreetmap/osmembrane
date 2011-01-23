package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import de.osmembrane.model.parser.ParserFactory;
import de.osmembrane.model.persistence.AbstractPersistence;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.persistence.OSMembranePersistence;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.pipeline.PipelineObserverObject.ChangeType;
import de.osmembrane.resources.Constants;

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

	private boolean savedState = true;

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
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.ADD_FUNCTION, func));
	}

	@Override
	public boolean deleteFunction(AbstractFunction func) {
		boolean returnValue = false;

		for (AbstractFunction function : functions) {
			if (function == func) {
				function.unlinkConnectors();
				returnValue = functions.remove(function);
				break;
			}
		}

		if (returnValue == true) {
			/* notify the observers */
			changedNotifyObservers(new PipelineObserverObject(
					ChangeType.DELETE_FUNCTION, func));
		}
		return returnValue;
	}

	@Override
	public void clear() {
		this.functions.clear();
		savedState = true;

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null));
	}

	@Override
	public void savePipeline(String filename) throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(OSMembranePersistence.class);

		persistence.save(filename, functions);

		/* Saved successfully (persistence has not thrown a FileException */
		savedState = true;
	}

	@Override
	public void backupPipeline() throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(OSMembranePersistence.class);

		persistence.save(Constants.DEFAULT_BACKUP_FILE, functions);
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
		return ParserFactory.getInstance().getParser(filetype.getParserClass())
				.parsePipeline(functions);
	}

	@Override
	public boolean hasLoop() {
		TarjanAlgorithm check = new TarjanAlgorithm(functions);
		check.run();

		/* check the SCCs */
		List<List<AbstractFunction>> sccs = check.getSCC();
		
		for (List<AbstractFunction> scc : sccs) {
			if (scc.size() == 1) {
				/*
				 * check if the scc with size 1 links to itself or is just
				 * standing alone
				 */
				AbstractFunction function = scc.get(0);
				for (AbstractConnector outConnector : function.getOutConnectors()) {
					for(AbstractConnector inConnector : outConnector.getConnections()) {
						if (inConnector.getParent() == function) {
							/* found a connection to the function itself */
							return true;
						}
					}
				}
			} else if (scc.size() > 1) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean isSaved() {
		return savedState;
	}

	@Override
	public void optimizePipeline() {
		/* TODO Implement a graph optimize algorithm */
	}

	@Override
	public boolean undo() {
		/* TODO Implement the undo/redo features */
		return false;
	}

	@Override
	public boolean undoAvailable() {
		/* TODO Implement the undo/redo features */
		return false;
	}

	@Override
	public boolean redo() {
		/* TODO Implement the undo/redo features */
		return false;
	}

	@Override
	public boolean redoAvailable() {
		/* TODO Implement the undo/redo features */
		return false;
	}

	@Override
	public void update(Observable o, Object arg) {

		/* only PipelineObserverObjects will be passed through */
		if (arg instanceof PipelineObserverObject) {
			changedNotifyObservers((PipelineObserverObject) arg);
		}
	}

	@Override
	protected void changedNotifyObservers(PipelineObserverObject poo) {
		poo.setPipeline(this);
		this.setChanged();
		this.notifyObservers(poo);

		/* any changes made, set savedState to false */
		savedState = false;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}
}