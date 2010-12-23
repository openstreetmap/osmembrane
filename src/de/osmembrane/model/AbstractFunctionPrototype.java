package de.osmembrane.model;

import java.util.Observable;

public abstract class AbstractFunctionPrototype extends Observable {

	public abstract void initiate(String xmlFilename);

	public abstract AbstractFunctionGroup[] getFunctionGroups();

	public abstract AbstractFunctionGroup getFunctionGroup(AbstractFunctionGroup group);

	public abstract AbstractFunction[] getFunctions(AbstractFunctionGroup group);

	public abstract AbstractFunction getFunction(AbstractFunction function);
}