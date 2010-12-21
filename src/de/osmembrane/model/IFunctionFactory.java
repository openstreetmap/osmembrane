package de.osmembrane.model;

public interface IFunctionFactory {

	public void initiate(String aXmlFilename);

	public IFunctionGroup[] getFunctionGroups();

	public IFunctionGroup getFunctionGroup(IFunctionGroup aGroup);

	public IFunction[] getFunctions(IFunctionGroup aGroup);

	public IFunction getFunction(IFunction aFunction);
}