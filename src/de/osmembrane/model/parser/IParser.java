package de.osmembrane.model.parser;

import de.osmembrane.model.Pipeline;

public interface IParser {

	public Pipeline parseString(String input);

	public String parsePipeline(Pipeline pipeline);
}