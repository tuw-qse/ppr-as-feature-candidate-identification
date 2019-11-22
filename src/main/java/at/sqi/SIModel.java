package at.sqi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.AttributeType;
import org.jgrapht.io.ComponentAttributeProvider;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.DefaultAttribute;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;

import at.sqi.model.FeatureCandidate;
import at.sqi.model.Process;
import at.sqi.model.Product;
import at.sqi.model.Resource;

public class SIModel {
	private List<FeatureCandidate> featureCandidates = new ArrayList<>();

	private HashMap<String, Process> processMap = new HashMap<>();
	private HashMap<String, Product> productMap = new HashMap<>();
	private HashMap<String, Resource> resourceMap = new HashMap<>();

	public void buildModel(List<List<String>> bigF) {
		extracteFeatureCandidateObjects(bigF);

		Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

		buildGraph(graph);

		GraphExporter<String, DefaultEdge> exporter = new DOTExporter<>(vertexIdProvider, vertexLabelProvider, null,
				vertexAttributeProvider, null);
		try {
			exporter.exportGraph(graph, new File("export.dot"));
		} catch (ExportException e) {
			e.printStackTrace();
		}
	}

	ComponentNameProvider<String> vertexIdProvider = new ComponentNameProvider<String>() {
		public String getName(String name) {
			String id = name.replaceAll("/", "_").replaceAll("\\(", "_").replaceAll("\\)", "_");
			return id;
		}
	};

	ComponentNameProvider<String> vertexLabelProvider = new ComponentNameProvider<String>() {
		public String getName(String name) {
			return name;
		}
	};

	ComponentAttributeProvider<String> vertexAttributeProvider = new ComponentAttributeProvider<String>() {
		String[] colors = { "black", "red", "green", "blue", "yellow" };

		@Override
		public Map<String, Attribute> getComponentAttributes(String component) {
			HashMap<String, Attribute> map = new HashMap<>();

			if (processMap.containsKey(component)) {
				map.put("shape", new DefaultAttribute<String>("rectangle", AttributeType.STRING));
			} else if (productMap.containsKey(component)) {
				map.put("shape", new DefaultAttribute<String>("oval", AttributeType.STRING));
			} else if (resourceMap.containsKey(component)) {
				map.put("shape", new DefaultAttribute<String>("rectangle", AttributeType.STRING));
				map.put("style", new DefaultAttribute<String>("rounded", AttributeType.STRING));
			}

			List<String> cont = new ArrayList<>();
			for (FeatureCandidate feature : featureCandidates) {
				cont.addAll(feature.getProcesses().keySet());
				cont.addAll(feature.getProducts().keySet());
				cont.addAll(feature.getResources().keySet());

				if (feature.getProcesses().containsKey(component) || feature.getProducts().containsKey(component)
						|| feature.getResources().containsKey(component)) {
					map.put("color",
							new DefaultAttribute<String>(colors[feature.getFeatureNumber()], AttributeType.STRING));
				}

				if (Collections.frequency(cont, component) > 1) {
					map.put("color", new DefaultAttribute<String>("black", AttributeType.STRING));
				}
			}

			return map;
		}
	};

	private void buildGraph(Graph<String, DefaultEdge> graph) {
		for (FeatureCandidate feature : featureCandidates) {
			feature.getProcesses().entrySet().forEach(processEntry -> {
				graph.addVertex(processEntry.getValue().getProcessName());
				processEntry.getValue().getOutputProducts().forEach(outputProduct -> {
					graph.addVertex(outputProduct);
					graph.addEdge(processEntry.getValue().getProcessName(), outputProduct);
				});
			});

			for (Entry<String, Product> productEntry : feature.getProducts().entrySet()) {
				Product product = productEntry.getValue();
				for (Entry<String, Process> process : this.processMap.entrySet()) {
					process.getValue().getOutputProducts().forEach(p -> {
						if (p.equals(product.getOutProductName())) {
							graph.addVertex(product.getInputProductName());
							graph.addEdge(product.getInputProductName(), process.getKey());
						}
					});
				}
			}

			feature.getResources().entrySet().forEach(resourceEntry -> {
				graph.addVertex(resourceEntry.getValue().getResourceName());
				graph.addEdge(resourceEntry.getValue().getResourceName(), resourceEntry.getValue().getProcessName());
			});
		}
	}

	private void extracteFeatureCandidateObjects(List<List<String>> bigF) {
		int index = 0;
		for (List<String> f : bigF) {
			HashMap<String, Process> processes = new HashMap<>();
			HashMap<String, Product> products = new HashMap<>();
			HashMap<String, Resource> resources = new HashMap<>();

			featureCandidates.add(new FeatureCandidate(index, processes, products, resources));

			for (String constructionPrimitve : f) {
				String[] splitPrimitive = constructionPrimitve.split("'");
				if (constructionPrimitve.startsWith("process")) {
					buildProcessFromConstructionPrimitive(splitPrimitive, processes);
				} else if (constructionPrimitve.startsWith("input2output")) {
					buildProductFromConstructionPrimitive(splitPrimitive, products);
				} else if (constructionPrimitve.startsWith("resource")) {
					buildResourceFromConstructionPrimitive(splitPrimitive, resources);
				}
			}
			index++;
		}
	}

	private Process buildProcessFromConstructionPrimitive(String[] splitPrimitive, HashMap<String, Process> processes) {
		Process process;
		String processName = splitPrimitive[1];
		String outputProductName = splitPrimitive[3];

		if (processes.containsKey(processName)) {
			process = processes.get(processName);
			process.getOutputProducts().add(outputProductName);
		} else {
			List<String> outputs = new ArrayList<>();
			outputs.add(outputProductName);
			process = new Process(processName, outputs);
			processes.put(processName, process);
		}
		this.processMap.put(processName, process);
		return process;
	}

	private Product buildProductFromConstructionPrimitive(String[] splitPrimitive, HashMap<String, Product> products) {
		Product product = null;
		String inputProductName = splitPrimitive[1];
		String outputProductName = splitPrimitive[3];

		if (!products.containsKey(inputProductName)) {
			product = new Product(inputProductName, outputProductName);
			products.put(inputProductName, product);
		}
		this.productMap.put(inputProductName, product);
		return product;
	}

	private Resource buildResourceFromConstructionPrimitive(String[] splitPrimitive,
			HashMap<String, Resource> resources) {
		Resource resource = null;
		String resourceName = splitPrimitive[1];
		String processName = splitPrimitive[3];
		String inputProductName = splitPrimitive[5];

		if (resources.containsKey(resourceName)) {
			resource = resources.get(resourceName);
			if (resource.getProcessName().equals(processName)) {
				resource.getInputProductNames().add(inputProductName);
			}
		} else {
			List<String> inputProducts = new ArrayList<>();
			inputProducts.add(inputProductName);
			resource = new Resource(resourceName, processName, inputProducts);
			resources.put(resourceName, resource);
		}
		this.resourceMap.put(resourceName, resource);
		return resource;
	}

}
