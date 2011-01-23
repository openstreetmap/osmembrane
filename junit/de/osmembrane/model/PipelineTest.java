/**
 * 
 */
package de.osmembrane.model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.rmi.server.ExportException;
import java.util.Map;

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
import de.osmembrane.model.pipeline.Function;
import de.osmembrane.model.pipeline.Task;
import de.osmembrane.model.xml.XMLTask;
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
		assertEquals(1, from.getOutConnectors()[0].getConnections().length);
		assertEquals(to,
				from.getOutConnectors()[0].getConnections()[0].getParent());

		assertEquals(1, to.getInConnectors()[0].getConnections().length);
		assertEquals(from,
				to.getInConnectors()[0].getConnections()[0].getParent());
	}

	/**
	 * Asserts there is no connection from from to to.
	 * 
	 * @param from
	 * @param to
	 */
	protected void assertNoConnection(AbstractFunction from, AbstractFunction to) {
		for (AbstractConnector ac : from.getOutConnectors()[0].getConnections()) {
			if (ac.getParent().equals(to)) {
				fail();
			}
		}

		for (AbstractConnector ac : to.getInConnectors()[0].getConnections()) {
			if (ac.getParent().equals(from)) {
				fail();
			}
		}

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
		assertEquals(loc, af.getCoordinate());
	}

	/**
	 * Asserts af's task is task.
	 * 
	 * @param af
	 * @param task
	 */
	protected void assertTask(AbstractFunction af, AbstractTask task) {
		assertEquals(task, af.getActiveTask());
	}

	/**
	 * Asserts the parameter param of af is value.
	 * 
	 * @param af
	 * @param param
	 * @param value
	 */
	protected void assertParameter(AbstractFunction af, int param, String value) {
		assertEquals(value,
				af.getActiveTask().getParameters()[param].getValue());
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

		assertEquals("not exactly 1 function present", 1,
				pl.getFunctions().length);
		assertEquals("not the correct function on the pipeline", newFunc,
				pl.getFunctions()[0]);
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

		assertEquals("more than 0 functions present", 0,
				pl.getFunctions().length);
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
	 * Creates a small, cozy pipeline.
	 * 
	 * @throws ConnectorException
	 */
	private void examplePipeline() throws ConnectorException {
		AbstractFunction[] newFuncs = new AbstractFunction[3];
		for (int i = 0; i < 3; i++) {
			newFuncs[i] = prototype.copy(CopyType.WITHOUT_VALUES_AND_POSITION);
			newFuncs[i].setCoordinate(new Point(i, 1));
		}

		pl.addFunction(newFuncs[0]);
		pl.addFunction(newFuncs[1]);
		pl.addFunction(newFuncs[2]);

		newFuncs[0].addConnectionTo(newFuncs[2]);

		newFuncs[1].setActiveTask(newFuncs[1].getAvailableTasks()[1]);
		newFuncs[1].getActiveTask().getParameters()[0].setValue(TEST_FILE_NAME);
	}

	/**
	 * Asserts the pipeline is small, warm and cozy.
	 */
	private void assertExamplePipeline() {
		// recreate the order using the locations
		AbstractFunction[] newFuncs = new AbstractFunction[3];
		for (int i = 0; i < pl.getFunctions().length; i++) {
			AbstractFunction af = pl.getFunctions()[i];
			Point p = (Point) af.getCoordinate();
			if (p.y == 1) {
				newFuncs[p.x] = af;
			}
		}

		assertNotNull(newFuncs[0]);
		assertNotNull(newFuncs[1]);
		assertNotNull(newFuncs[2]);

		assertSingleConnection(newFuncs[0], newFuncs[2]);
		assertNoConnection(newFuncs[0], newFuncs[1]);
		assertNoConnection(newFuncs[1], newFuncs[2]);

		assertTask(newFuncs[1], newFuncs[1].getAvailableTasks()[1]);
		assertParameter(newFuncs[1], 0, TEST_FILE_NAME);
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
		examplePipeline();
		assertExamplePipeline();

		pl.savePipeline(TEST_FILE_NAME);
		pl.clear();
		pl.loadPipeline(TEST_FILE_NAME);

		assertExamplePipeline();
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
		examplePipeline();
		assertExamplePipeline();

		pl.backupPipeline();
		pl.clear();
		pl.loadPipeline(Constants.DEFAULT_BACKUP_FILE);

		assertExamplePipeline();
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
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGenerate() throws IllegalArgumentException, IllegalAccessException {
		AbstractFunction af = prototype.copy(CopyType.COPY_ALL);
		pl.addFunction(af);
		
		String result = "";//pl.generate(FileType.BASH);
		 
		assertNotNull(result);
		XMLTask xmlt = null;
		
		// Oh, look out! There's a bear behind you! *Scrolling down*
		Task t = (Task) af.getActiveTask();
		for (Field f : t.getClass().getDeclaredFields()) {
			if (f.getType() == XMLTask.class) {
				f.setAccessible(true);
				xmlt = (XMLTask) f.get(new XMLTask());
				// TODO breaks with UnsafeObjectFieldAccessorImpl ?
				break;
			}
		}
		// Huh? What has happened? I don't know...
		
		if (xmlt.getShortName() != null) {
			assertTrue(result.contains(xmlt.getShortName()));
		} else {
			assertTrue(result.contains(xmlt.getName()));
		}
	}

	/**
	 * Test method for {@link de.osmembrane.model.pipeline.Pipeline#isSaved()}.
	 * @throws FileException 
	 */
	@Test
	public void testIsSaved() throws FileException {
		pl.clear();
		assertTrue("pipeline empty, but not saved. huh?", pl.isSaved());

		pl.addFunction(prototype.copy(CopyType.WITHOUT_VALUES_AND_POSITION));
		assertFalse("pipeline changed without saving, but saved. huh?",
				pl.isSaved());
		
		pl.savePipeline(TEST_FILE_NAME);
		assertTrue("pipeline saved, but not saved. huh?", pl.isSaved());
		
		
	}

}
