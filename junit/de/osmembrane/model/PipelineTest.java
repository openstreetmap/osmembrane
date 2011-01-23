/**
 * 
 */
package de.osmembrane.model;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.AASTORE;

import de.osmembrane.Application;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.AbstractFunctionPrototype;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.AbstractTask;
import de.osmembrane.model.pipeline.ConnectorException;
import de.osmembrane.model.pipeline.ConnectorType;
import de.osmembrane.model.pipeline.CopyType;
import de.osmembrane.resources.Constants;

/**
 * Tests the pipeline.
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineTest {

	private static AbstractFunction prototype;

	/**
	 * pipeline under test
	 */
	private static AbstractPipeline pl;

	private static String TEST_FILE_NAME = System.getProperty("java.io.tmpdir")
			+ "test.tmp";

	/**
	 * Initiates a full testable {@link Application}, then selects the first
	 * function to satisfy the conditions, that
	 * 
	 * <ul>
	 * <li>at least 1 in-connector</li>
	 * <li>at least 1 out-connector</li>
	 * <li>the first in-connector is of type entity</li>
	 * <li>the first out-connector is of type entity</li>
	 * <li>the first in-connector allows exactly 1 connection</li>
	 * <li>at least 2 tasks available</li>
	 * </ul>
	 * 
	 * Tests are performed on this function prototype then
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Application a = new Application();
		a.createModels();
		a.initiate();

		pl = ModelProxy.getInstance().accessPipeline();

		AbstractFunctionPrototype afp = ModelProxy.getInstance()
				.accessFunctions();

		for (AbstractFunctionGroup afg : afp.getFunctionGroups()) {
			for (AbstractFunction af : afg.getFunctions()) {

				if ((af.getInConnectors() != null)
						&& (af.getInConnectors().length > 0)
						&& (af.getOutConnectors() != null)
						&& (af.getOutConnectors().length > 0)) {

					AbstractConnector firstIn = af.getInConnectors()[0];
					AbstractConnector firstOut = af.getOutConnectors()[0];

					if ((firstIn.getType() != ConnectorType.ENTITY)
							|| (firstOut.getType() != ConnectorType.ENTITY)
							|| (firstIn.getMaxConnections() != 1)
							|| (af.getAvailableTasks().length < 2)) {
						continue;
					}

					prototype = af;

					return;

				}
			} /* for */
		} /* for */

		fail("No suitable function for testing found! Check the osmdefinitions!");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Cleans the pipeline after each test
	 */
	@After
	public void tearDown() {
		ModelProxy.getInstance().accessPipeline().clear();
	}

	/**
	 * Asserts there is only 1 single connection from from to to.
	 * 
	 * @param from
	 * @param to
	 */
	protected void assertSingleConnection(AbstractFunction from,
			AbstractFunction to) {
		assertTrue(from.getOutConnectors()[0].getConnections().length == 1);
		assertEquals(from.getOutConnectors()[0].getConnections()[0], to);

		assertTrue(to.getInConnectors()[0].getConnections().length == 1);
		assertEquals(to.getInConnectors()[0].getConnections()[0], from);
	}

	/**
	 * Asserts there is no connection from from to to.
	 * 
	 * @param from
	 * @param to
	 */
	protected void assertNoConnection(AbstractFunction from, AbstractFunction to) {
		assertTrue(from.getOutConnectors()[0].getConnections().length == 0);
		assertTrue(to.getInConnectors()[0].getConnections().length == 0);
	}

	/**
	 * Asserts af is on the pipeline, iff onPipeline == true
	 * 
	 * @param af
	 * @param onPipeline
	 */
	protected void assertOnPipeline(AbstractFunction af, boolean onPipeline) {

		for (AbstractFunction afLoop : pl.getFunctions()) {
			if (afLoop.equals(af)) {

				if (onPipeline) {
					// should be here
					return;
				} else {
					// should not be here
					fail();
				}
			}
		} /* for */

		if (onPipeline) {
			// should be on the pipeline
			fail();
		}
	}

	/**
	 * Asserts af is at loc.
	 * 
	 * @param af
	 * @param loc
	 */
	protected void assertAtLocation(AbstractFunction af, Point2D loc) {
		assertEquals(af.getCoordinate(), loc);
	}

	/**
	 * Asserts af's task is task.
	 * 
	 * @param af
	 * @param task
	 */
	protected void assertTask(AbstractFunction af, AbstractTask task) {
		assertEquals(af.getActiveTask(), task);
	}

	/**
	 * Asserts the parameter param of af is value.
	 * 
	 * @param af
	 * @param param
	 * @param value
	 */
	protected void assertParameter(AbstractFunction af, int param, String value) {
		assertEquals(af.getActiveTask().getParameters()[param].getValue(),
				value);
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#addFunction(de.osmembrane.model.pipeline.AbstractFunction)}
	 * .
	 */
	@Test
	public void testAddFunction() {
		AbstractFunction newFunc = prototype
				.copy(CopyType.WITHOUT_VALUES_AND_POSITION);
		pl.addFunction(newFunc);

		assertTrue("not exactly 1 function present",
				pl.getFunctions().length == 1);
		assertEquals("not the correct function on the pipeline",
				pl.getFunctions()[0], newFunc);
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#deleteFunction(de.osmembrane.model.pipeline.AbstractFunction)}
	 * .
	 */
	@Test
	public void testDeleteFunction() {
		AbstractFunction newFunc = prototype
				.copy(CopyType.WITHOUT_VALUES_AND_POSITION);
		pl.addFunction(newFunc);
		pl.deleteFunction(newFunc);

		assertTrue("more than 0 functions present",
				pl.getFunctions().length == 0);
	}

	/**
	 * Test method for {@link de.osmembrane.model.pipeline.Pipeline#undo()} and
	 * {@link de.osmembrane.model.pipeline.Pipeline#redo()}.
	 * 
	 * @see Spezifikation 5.2.1, 5.2.2
	 * 
	 *      I bet this thing covers most of the model in one go.
	 * 
	 * @throws ConnectorException
	 * @throws FileException
	 */
	@Test
	public void testUndoRedo() throws ConnectorException, FileException {
		AbstractFunction[] newFuncs = new AbstractFunction[3];
		for (int i = 0; i < 2; i++) {
			newFuncs[i] = prototype.copy(CopyType.WITHOUT_VALUES_AND_POSITION);
		}

		final Point2D locationA = new Point2D.Double(1.0, 1.0);
		final Point2D locationB = new Point2D.Double(2.0, 2.0);

		final AbstractTask task1A = newFuncs[1].getAvailableTasks()[0];
		final AbstractTask task1B = newFuncs[1].getAvailableTasks()[1];

		final String paramA = "osmembrane";
		final String paramB = "TESTING";

		/*
		 * Notation of values expected to be actual: ID. 012 LTPU CD
		 * 
		 * ID = number describing the amount of steps taken. 0, 1, 2 =
		 * newFunc[0, 1, 2] is on the pipeline
		 * 
		 * L = location of newFunc[0] T = task of newFunc[1] P = parameter of
		 * newFunc[2] U = task of newFunc[2] (x = not applicable, A = value A, B
		 * = value B)
		 * 
		 * C = connection from 0 to 1 D = connection from 1 to 2
		 */

		/* 00. ___ xxxx __ */

		pl.addFunction(newFuncs[0]);
		newFuncs[0].setCoordinate(locationA);
		/* 01. 0__ Axxx __ */

		pl.addFunction(newFuncs[1]);
		newFuncs[1].setActiveTask(task1B);
		/* 02. 01_ ABxx __ */

		newFuncs[0].addConnectionTo(newFuncs[1]);
		/* 03. 01_ ABxx C_ */

		newFuncs[1].getActiveTask().getParameters()[0].setValue(paramA);
		// should copy this as well.
		newFuncs[2] = newFuncs[1].copy(CopyType.COPY_ALL);
		final AbstractTask task2A = newFuncs[2].getAvailableTasks()[0];
		final AbstractTask task2B = newFuncs[2].getAvailableTasks()[1];
		pl.addFunction(newFuncs[2]);
		/* 04. 012 ABAB C_ */

		newFuncs[1].setActiveTask(task1A);
		/* 05. 012 AAAB C_ */

		newFuncs[1].addConnectionTo(newFuncs[2]);
		/* 06. 012 AAAB CD */

		newFuncs[0].setCoordinate(locationB);
		/* 07. 012 BAAB CD */

		newFuncs[2].setActiveTask(task2A);
		/* 08. 012 BAAA CD */

		newFuncs[2].getActiveTask().getParameters()[0].setValue(paramB);
		/* 09. 012 BABA CD */

		pl.deleteFunction(newFuncs[2]);
		/* 10. 01_ BAxx C_ */

		newFuncs[0].removeConnectionTo(newFuncs[1]);
		/* 11. 01_ BAxx __ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], false);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		// parameter 2 is not applicable
		// task 2 is not applicable
		assertNoConnection(newFuncs[0], newFuncs[1]);
		// connection 1-2 not applicable

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 10. 01_ BAxx C_ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], false);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		// parameter 2 is not applicable
		// task 2 is not applicable
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		// connection 1-2 not applicable

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 09. 012 BABA CD */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		assertParameter(newFuncs[2], 0, paramB);
		assertTask(newFuncs[2], task2A);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertSingleConnection(newFuncs[1], newFuncs[2]);

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 08. 012 BAAA CD */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		assertParameter(newFuncs[2], 0, paramA);
		assertTask(newFuncs[2], task2A);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertSingleConnection(newFuncs[1], newFuncs[2]);

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 07. 012 BAAB CD */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		assertParameter(newFuncs[2], 0, paramA);
		assertTask(newFuncs[2], task2B);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertSingleConnection(newFuncs[1], newFuncs[2]);

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 06. 012 AAAB CD */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationA);
		assertTask(newFuncs[1], task1A);
		assertParameter(newFuncs[2], 0, paramA);
		assertTask(newFuncs[2], task2B);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertSingleConnection(newFuncs[1], newFuncs[2]);

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 05. 012 AAAB C_ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationA);
		assertTask(newFuncs[1], task1A);
		assertParameter(newFuncs[2], 0, paramA);
		assertTask(newFuncs[2], task2B);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertNoConnection(newFuncs[1], newFuncs[2]);

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 04. 012 ABAB C_ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationA);
		assertTask(newFuncs[1], task1B);
		assertParameter(newFuncs[2], 0, paramA);
		assertTask(newFuncs[2], task2B);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertNoConnection(newFuncs[1], newFuncs[2]);

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 03. 01_ ABxx C_ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], false);
		assertAtLocation(newFuncs[0], locationA);
		assertTask(newFuncs[1], task1B);
		// parameter 2 is not applicable
		// task 2 is not applicable
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		// connection 1-2 not applicable

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 02. 01_ ABxx __ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], false);
		assertAtLocation(newFuncs[0], locationA);
		assertTask(newFuncs[1], task1B);
		// parameter 2 is not applicable
		// task 2 is not applicable
		assertNoConnection(newFuncs[0], newFuncs[1]);
		// connection 1-2 not applicable

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 01. 0__ Axxx __ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], false);
		assertOnPipeline(newFuncs[2], false);
		assertAtLocation(newFuncs[0], locationA);
		// task 1 is not applicable
		// parameter 2 is not applicable
		// task 2 is not applicable
		// connection 0-1 not applicable
		// connection 1-2 not applicable

		assertTrue("undo was not available", pl.undoAvailable());
		assertTrue("undo could not be done", pl.undo());
		/* 00. ___ xxxx __ */

		assertOnPipeline(newFuncs[0], false);
		assertOnPipeline(newFuncs[1], false);
		assertOnPipeline(newFuncs[2], false);
		// location 0 is not applicable
		// task 1 is not applicable
		// parameter 2 is not applicable
		// task 2 is not applicable
		// connection 0-1 not applicable
		// connection 1-2 not applicable

		// does this count as a change? -> No, eclipse behaves the same way :)
		assertTrue("pipeline is the same, but has changed?", pl.isSaved());
		// saving should not corrupt the undo/redo
		pl.savePipeline(TEST_FILE_NAME);

		// we now trust that the single steps perform correctly.
		// so perform most redos at once.

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 01. 0__ Axxx __ */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 02. 01_ ABxx __ */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 03. 01_ ABxx C_ */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 04. 012 ABAB C_ */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 05. 012 AAAB C_ */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 06. 012 AAAB CD */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 07. 012 BAAB CD */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 08. 012 BAAA CD */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 09. 012 BABA CD */

		// I'd like to assert here too, since changes might get lost in 10 and
		// 11
		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		assertParameter(newFuncs[2], 0, paramB);
		assertTask(newFuncs[2], task2A);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertSingleConnection(newFuncs[1], newFuncs[2]);

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 10. 01_ BAxx C_ */

		assertTrue("redo was not available", pl.redoAvailable());
		assertTrue("redo could not be done", pl.redo());
		/* 11. 01_ BAxx __ */

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], false);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		// parameter 2 is not applicable
		// task 2 is not applicable
		assertNoConnection(newFuncs[0], newFuncs[1]);
		// connection 1-2 not applicable

	}

	/**
	 * Performs the same steps as undo redo test to step 9.
	 * 
	 * @return assertable object array
	 * 
	 * @throws ConnectorException
	 */
	private Object[] performUndoRedoTestToStep9() throws ConnectorException {
		AbstractFunction[] newFuncs = new AbstractFunction[3];
		for (int i = 0; i < 2; i++) {
			newFuncs[i] = prototype.copy(CopyType.WITHOUT_VALUES_AND_POSITION);
		}

		final Point2D locationA = new Point2D.Double(1.0, 1.0);
		final Point2D locationB = new Point2D.Double(2.0, 2.0);

		final AbstractTask task1A = newFuncs[1].getAvailableTasks()[0];
		final AbstractTask task1B = newFuncs[1].getAvailableTasks()[1];

		final String paramA = "osmembrane";
		final String paramB = "TESTING";

		/* 00. ___ xxxx __ */

		pl.addFunction(newFuncs[0]);
		newFuncs[0].setCoordinate(locationA);
		/* 01. 0__ Axxx __ */

		pl.addFunction(newFuncs[1]);
		newFuncs[1].setActiveTask(task1B);
		/* 02. 01_ ABxx __ */

		newFuncs[0].addConnectionTo(newFuncs[1]);
		/* 03. 01_ ABxx C_ */

		newFuncs[1].getActiveTask().getParameters()[0].setValue(paramA);
		// should copy this as well.
		newFuncs[2] = newFuncs[1].copy(CopyType.COPY_ALL);
		final AbstractTask task2A = newFuncs[2].getAvailableTasks()[0];
		final AbstractTask task2B = newFuncs[2].getAvailableTasks()[1];
		pl.addFunction(newFuncs[2]);
		/* 04. 012 ABAB C_ */

		newFuncs[1].setActiveTask(task1A);
		/* 05. 012 AAAB C_ */

		newFuncs[1].addConnectionTo(newFuncs[2]);
		/* 06. 012 AAAB CD */

		newFuncs[0].setCoordinate(locationB);
		/* 07. 012 BAAB CD */

		newFuncs[2].setActiveTask(task1A);
		/* 08. 012 BAAA CD */

		newFuncs[2].getActiveTask().getParameters()[0].setValue(paramB);
		/* 09. 012 BABA CD */

		Object[] result = new Object[11];
		result[0] = newFuncs[0];
		result[1] = newFuncs[1];
		result[2] = newFuncs[2];
		result[3] = locationA;
		result[4] = locationB;
		result[5] = task1A;
		result[6] = task1B;
		result[7] = task2A;
		result[8] = task2B;
		result[9] = paramA;
		result[10] = paramB;
		return result;
	}

	/**
	 * Asserts the same things as undo redo test to step 9.
	 */
	private void assertUndoRedoTestToStep9(Object[] assertArray) {
		AbstractFunction[] newFuncs = new AbstractFunction[3];
		newFuncs[0] = (AbstractFunction) assertArray[0];
		newFuncs[1] = (AbstractFunction) assertArray[1];
		newFuncs[2] = (AbstractFunction) assertArray[2];

		final Point2D locationA = (Point2D) assertArray[3];
		final Point2D locationB = (Point2D) assertArray[4];

		final AbstractTask task1A = (AbstractTask) assertArray[5];
		final AbstractTask task1B = (AbstractTask) assertArray[6];
		
		final AbstractTask task2A = (AbstractTask) assertArray[7];
		final AbstractTask task2B = (AbstractTask) assertArray[8];
		
		final String paramA = (String) assertArray[9];
		final String paramB = (String) assertArray[10];

		assertOnPipeline(newFuncs[0], true);
		assertOnPipeline(newFuncs[1], true);
		assertOnPipeline(newFuncs[2], true);
		assertAtLocation(newFuncs[0], locationB);
		assertTask(newFuncs[1], task1A);
		assertParameter(newFuncs[2], 0, paramB);
		assertTask(newFuncs[2], task2A);
		assertSingleConnection(newFuncs[0], newFuncs[1]);
		assertSingleConnection(newFuncs[1], newFuncs[2]);
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#optimizePipeline()}.
	 */
	@Test
	public void testOptimizePipeline() throws FileException, ConnectorException {
		pl.optimizePipeline();
		fail("No idea what this is and how to test it");
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#savePipeline(java.lang.String)}
	 * and
	 * {@link de.osmembrane.model.pipeline.Pipeline#loadPipeline(java.lang.String)}
	 * .
	 * 
	 * @throws ConnectorException
	 * @throws FileException
	 */
	@Test
	public void testSaveLoadPipeline() throws ConnectorException, FileException {
		Object[] assertion = performUndoRedoTestToStep9();
		assertUndoRedoTestToStep9(assertion);

		pl.savePipeline(TEST_FILE_NAME);
		pl.clear();
		pl.loadPipeline(TEST_FILE_NAME);

		assertUndoRedoTestToStep9(assertion);
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#backupPipeline()} and
	 * {@link de.osmembrane.model.pipeline.Pipeline#loadPipeline(java.lang.String)}
	 * .
	 * 
	 * @throws ConnectorException
	 * @throws FileException
	 */
	@Test
	public void testBackupLoadPipeline() throws ConnectorException,
			FileException {
		Object[] assertion = performUndoRedoTestToStep9();
		assertUndoRedoTestToStep9(assertion);

		pl.backupPipeline();
		pl.clear();
		pl.loadPipeline(Constants.DEFAULT_BACKUP_FILE);

		assertUndoRedoTestToStep9(assertion);
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#importPipeline(java.lang.String, de.osmembrane.model.persistence.FileType)}
	 * .
	 */
	@Test
	public void testImportPipeline() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#exportPipeline(java.lang.String, de.osmembrane.model.persistence.FileType)}
	 * .
	 */
	@Test
	public void testExportPipeline() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link de.osmembrane.model.pipeline.Pipeline#generate(de.osmembrane.model.persistence.FileType)}
	 * .
	 */
	@Test
	public void testGenerate() {
		pl.generate(FileType.BASH);
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.osmembrane.model.pipeline.Pipeline#isSaved()}.
	 */
	@Test
	public void testIsSaved() {
		pl.clear();		
		assertTrue("pipeline empty, but not saved. huh?", pl.isSaved());
		
		pl.addFunction(prototype.copy(CopyType.WITHOUT_VALUES_AND_POSITION));
		assertFalse("pipeline changed without saving, but saved. huh?",
				pl.isSaved());
	}

}
