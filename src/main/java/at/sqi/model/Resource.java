package at.sqi.model;

import java.util.List;

public class Resource {
	private String resourceName;
	private String processName;
	private List<String> inputProductNames;

	public Resource(String resourceName, String processName, List<String> inputProductNames) {
		this.resourceName = resourceName;
		this.processName = processName;
		this.inputProductNames = inputProductNames;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getProcessName() {
		return processName;
	}

	public List<String> getInputProductNames() {
		return inputProductNames;
	}
}
