package at.sqi.model;

import java.util.HashMap;

public class FeatureCandidate {
	Integer featureNumber;
	HashMap<String, Process> processes = new HashMap<>();
	HashMap<String, Product> products = new HashMap<>();
	HashMap<String, Resource> resources = new HashMap<>();

	public FeatureCandidate(Integer featureNumber, HashMap<String, Process> processes,
			HashMap<String, Product> products, HashMap<String, Resource> resources) {
		this.featureNumber = featureNumber;
		this.processes = processes;
		this.products = products;
		this.resources = resources;
	}

	public Integer getFeatureNumber() {
		return featureNumber;
	}

	public HashMap<String, Process> getProcesses() {
		return processes;
	}

	public HashMap<String, Product> getProducts() {
		return products;
	}

	public HashMap<String, Resource> getResources() {
		return resources;
	}
}
