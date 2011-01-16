package de.osmembrane.model.pipeline;

public class PipelineObserverObject {
	
	public enum ChangeType {
		ADD,
		CHANGE,
		DELETE,
		FULLCHANGE
	};
	
	private ChangeType type;
	private AbstractFunction changedFunction;
	private AbstractPipeline pipeline;
	
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
	
	public void setPipeline(AbstractPipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	public AbstractPipeline getPipeline() {
		return pipeline;
	}
}