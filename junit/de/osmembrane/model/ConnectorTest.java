package de.osmembrane.model;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Observable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.osmembrane.Application;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.AbstractTask;
import de.osmembrane.model.pipeline.Connector;
import de.osmembrane.model.pipeline.ConnectorException;
import de.osmembrane.model.pipeline.ConnectorType;
import de.osmembrane.model.pipeline.Function;
import de.osmembrane.model.pipeline.Pipeline;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLPipe;

/**
 * Tests the connector.
 * 
 * @author tobias_kuhn
 * 
 */
public class ConnectorTest {

	private static AbstractFunction[] funcs = new AbstractFunction[3];

	private static AbstractConnector conOut, conIn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Application a = new Application();
		a.createModels();
		a.initiate();

		AbstractFunctionPrototype afp = ModelProxy.getInstance()
				.accessFunctions();

		funcs[0] = null;
		
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
							|| (firstIn.getMaxConnections() == 1)) {
						continue;
					}

					for (int i = 0; i < 3; i++) {
						funcs[i] = ModelProxy.getInstance().accessFunctions()
								.getFunction(af);
						ModelProxy.getInstance().accessPipeline()
								.addFunction(funcs[i]);
					}
					
					conOut = funcs[0].getOutConnectors()[0];
					conIn = funcs[1].getInConnectors()[0];

					break;

				}
			} /* for */
			
			if (funcs[0] != null) {
				break;
			}
		} /* for */
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetParent() {
		assertEquals("ConnectorTest:getParent()", funcs[0], conOut.getParent());
	}

	@Test
	public void testGetDescription() {
		assertNotNull("ConnectorTest:getDescription()", conOut.getDescription());
	}

	@Test
	public void testGetType() {
		assertEquals("ConnectorTest:getType()", ConnectorType.ENTITY,
				conOut.getType());
	}

	@Test
	public void testGetMaxConnections() {
		assertTrue("ConnectorTest:getMaxConnections()",
				(conOut.getMaxConnections() > 0) && (conIn.getMaxConnections() == 1));
	}

	@Test
	public void testIsFull() throws ConnectorException {
		funcs[0].addConnectionTo(funcs[1]);
		assertTrue("ConnectorTest:isFull()", conIn.isFull());
		funcs[0].removeConnectionTo(funcs[1]);
	}

	@Test(expected = ConnectorException.class)
	public void testAddConnection() throws ConnectorException {
		funcs[0].addConnectionTo(funcs[1]);
		assertTrue(
				"ConnectorTest:addConnection()",
				(conIn.getConnections().length == 1)
						&& (conOut.getConnections().length == 1));
		funcs[1].addConnectionTo(funcs[2]);
		funcs[2].addConnectionTo(funcs[0]);
		// here we want exception
	}

	@Test
	public void testRemoveConnection() {
		funcs[0].removeConnectionTo(funcs[1]);
		funcs[1].removeConnectionTo(funcs[2]);
		assertTrue(
				"ConnectorTest:removeConnection()",
				(conIn.getConnections().length == 0)
						&& (conOut.getConnections().length == 0));
	}

	@Test
	public void testGetConnections() throws ConnectorException {
		funcs[0].addConnectionTo(funcs[1]);

		assertTrue(
				"ConnectorTest:getConnections()",
				(conOut.getConnections().length == 1)
						&& (conOut.getConnections()[0].equals(conIn)));

	}

}
