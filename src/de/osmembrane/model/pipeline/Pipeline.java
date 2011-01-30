package de.osmembrane.model.pipeline;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

import de.osmembrane.model.algorithms.GraphPlanarizer;
import de.osmembrane.model.algorithms.TarjanAlgorithm;
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

	private Stack<PipelineMemento> undoStack;
	private PipelineMemento currentState;
	private Stack<PipelineMemento> redoStack;

	private List<AbstractFunction> functions;

	private boolean savedState = true;

	/**
	 * Constructor for {@link Pipeline}.
	 */
	public Pipeline(boolean silent) {
		this.functions = new ArrayList<AbstractFunction>();
		this.undoStack = new Stack<PipelineMemento>();
		this.redoStack = new Stack<PipelineMemento>();

		/* register the Observer of Persistence to the Pipeline */
		if (!silent) {
			addObserver(PersistenceFactory.getInstance());
		}
	}

	@Override
	public void clear() {
		this.functions.clear();
		this.undoStack.clear();
		this.currentState = new PipelineMemento(functions, savedState);
		this.redoStack.clear();

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null).setCreateUndoStep(false));

		changeSavedState(true);
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
		func.addObserver(this);
		functions.add(func);

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
	public void loadPipeline(URL filename) throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(OSMembranePersistence.class);

		Object obj = persistence.load(filename);

		/* is checked by persistence */
		@SuppressWarnings("unchecked")
		List<AbstractFunction> functions = (List<AbstractFunction>) obj;

		this.functions = functions;
		for (AbstractFunction function : functions) {
			function.addObserver(this);
		}

		/*
		 * Save the loaded Pipeline as the first undoStep which will be created
		 * at the next step.
		 */
		this.currentState = new PipelineMemento(functions, savedState);

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null).setCreateUndoStep(false));
	}

	@Override
	public void savePipeline(URL filename) throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(OSMembranePersistence.class);

		persistence.save(filename, functions);

		/* Saved successfully (persistence has not thrown a FileException */
		changeSavedState(true);
	}

	@Override
	public boolean isSaved() {
		return savedState;
	}

	@Override
	public void backupPipeline() throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(OSMembranePersistence.class);

		persistence.save(Constants.DEFAULT_BACKUP_FILE, functions);
	}

	@Override
	public void importPipeline(URL filename, FileType type)
			throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(type.getPersistenceClass());

		Object obj = persistence.load(filename);

		/* is checked by persistence */
		@SuppressWarnings("unchecked")
		List<AbstractFunction> functions = (List<AbstractFunction>) obj;

		this.functions = functions;
		for (AbstractFunction function : functions) {
			function.addObserver(this);
		}

		/*
		 * Save the imported Pipeline as the first undoStep which will be
		 * created at the next step.
		 */

		/* notify the observers */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null).setCreateUndoStep(false));
	}

	@Override
	public String generate(FileType filetype) {
		return ParserFactory.getInstance().getParser(filetype.getParserClass())
				.parsePipeline(functions);
	}

	@Override
	public void exportPipeline(URL filename, FileType type)
			throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(type.getPersistenceClass());

		persistence.save(filename, functions);
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
				for (AbstractConnector outConnector : function
						.getOutConnectors()) {
					for (AbstractConnector inConnector : outConnector
							.getConnections()) {
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
	public void arrangePipeline() {
		GraphPlanarizer gprizer = new GraphPlanarizer(functions);
		gprizer.planarize();
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null));
	}

	@Override
	public boolean undo() {
		if (!undoAvailable()) {
			return false;
		}

		redoStack.push(currentState);
		restoreMemento(undoStack.pop());

		return true;
	}

	@Override
	public boolean undoAvailable() {
		return !undoStack.isEmpty();
	}

	@Override
	public boolean redo() {
		if (!redoAvailable()) {
			return false;
		}

		undoStack.push(currentState);
		restoreMemento(redoStack.pop());

		return true;
	}

	@Override
	public boolean redoAvailable() {
		return !redoStack.isEmpty();
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
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

		if (poo.createUndoStep()) {
			/* any changes made, set savedState to false */
			changeSavedState(false);
		}

		/* Debug output, TODO remove that at final release */
		// Integer stackPeekSize = (Integer) null;
		// if (undoStack.size() > 0) {
		// stackPeekSize = undoStack.peek().getFunctions().size();
		// }
		//
		// System.out.println("undoStack.size = " + undoStack.size()
		// + "; redoStack.size = " + redoStack.size()
		// + "; functions.size = " + functions.size()
		// + "\ncurrentState.functions.size = "
		// + currentState.getFunctions().size()
		// + "; undoStack.peek.functions.size = " + stackPeekSize
		// + "\n lastAction = " + poo.getType() + "\n");
		/* End of Debug output */

		this.setChanged();
		this.notifyObservers(poo);
	}

	private void changeSavedState(boolean state) {
		this.savedState = state;

		if (state == false) {
			saveStep();
		} else {
			/*
			 * Update the savedState for the current item (nothing changed in
			 * the pipeline only the state should be updated.
			 */
			currentState = new PipelineMemento(functions, savedState);
		}
	}

	private void saveStep() {
		undoStack.push(currentState);
		redoStack.clear();
		currentState = new PipelineMemento(functions, savedState);
	}

	private void restoreMemento(PipelineMemento memento) {
		this.functions = memento.getFunctions();
		this.savedState = memento.getSavedState();
		this.currentState = memento;

		for (AbstractFunction function : functions) {
			function.addObserver(this);
		}

		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.FULLCHANGE, null).setCreateUndoStep(false));
	}
}

class PipelineMemento {

	private List<AbstractFunction> functions = new ArrayList<AbstractFunction>();
	private boolean savedState;

	public PipelineMemento(List<AbstractFunction> functions, boolean savedState) {
		this.functions = deepCopyFunctions(functions);
		this.savedState = savedState;
	}

	public boolean getSavedState() {
		return savedState;
	}

	public List<AbstractFunction> getFunctions() {
		return deepCopyFunctions(functions);
	}

	private List<AbstractFunction> deepCopyFunctions(
			List<AbstractFunction> functions) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(functions);
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object deepCopy = ois.readObject();

			@SuppressWarnings("unchecked")
			List<AbstractFunction> copy = (List<AbstractFunction>) deepCopy;
			return copy;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return functions;
	}
}