package de.osmembrane.model;

import org.junit.BeforeClass;
import org.junit.Test;

import de.osmembrane.Application;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.AbstractTask;
import de.osmembrane.model.pipeline.CopyType;

public class GroupCopyTest {

	private static AbstractFunctionPrototype afp;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Application a = new Application();
		a.createModels();
		a.initiate();

		afp = ModelProxy.getInstance().accessFunctions();
	}

	@Test
	public void testGroupCopy() {
		AbstractFunctionGroup afg = afp.getFunctionGroups()[0];
		
		afg.getFunctions()[0].getActiveTask().getParameters()[0].setValue("HALLO WELT!");

		print(afg);
		System.out.println("--------------------");
		print(afg.copy(CopyType.WITHOUT_VALUES));
	}

	private void print(AbstractFunctionGroup afg) {
		System.out.println(afg.getId() + " (" + afg.getFriendlyName() + ")");
		for (AbstractFunction function : afg.getFunctions()) {
			System.out.println("  " + function.getId() + " ("
					+ function.getFriendlyName() + ")");
			
			System.out.println("    Tasks:");
			for (AbstractTask task : function.getAvailableTasks()) {
				System.out.println("      " + task.getName() + " ("
						+ task.getFriendlyName() + ")");
				for (AbstractParameter param : task.getParameters()) {
					System.out.println("        " + param.getName() + " ("
							+ param.getFriendlyName() + "): "
							+ param.getValue() + "(" + param.getDefaultValue()
							+ ")");
				}
			}
			
			System.out.println("    In-Connectors:");
			for(AbstractConnector con : function.getInConnectors()) {
				System.out.println("        " + con.getType() + " (" + con.getConnections().length + " connections)");
			}
			System.out.println("    Out-Connectors:");
			for(AbstractConnector con : function.getOutConnectors()) {
				System.out.println("      " + con.getType() + " (" + con.getConnections().length + " connections)");
			}
		}
	}
}
