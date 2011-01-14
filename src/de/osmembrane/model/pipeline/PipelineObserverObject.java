package de.osmembrane.model.pipeline;

public class PipelineObserverObject {
	
	private ChangeType type;
	private AbstractFunction changedFunction;
	
	public PipelineObserverObject(ChangeType type, AbstractFunction changedFunction) {
		this.type = type;
		this.changedFunction = changedFunction;
	}
	
	public ChangeType getType() {
		return type;
	}
	
	public AbstractFunction getChangedFunction() {
		return changedFunction;
	}
}

enum ChangeType {
	ADD,
	CHANGE,
	DELETE,
	FULLCHANGE
}