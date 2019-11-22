package at.sqi.model;

import java.util.List;

public class Process {
	private String processName;
	private List<String> outputProductNames;
	
	public Process(String processName, List<String> outputProducts) {
		this.processName = processName;
		this.outputProductNames = outputProducts;
	}

	public String getProcessName() {
		return processName;
	}

	public List<String> getOutputProducts() {
		return outputProductNames;
	}
}
