package at.sqi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import at.sqi.model.ImmutableModelPrimitiveTuple;

public class FCI {
	private HashMap<String, String> constructionPrimitiveCodes = new HashMap<>();
	private HashMap<String, List<String>> constructionPrimitiveInModels = new HashMap<>();
	private HashMap<String, List<String>> modelsWithConstructionPrimitives = new HashMap<>();
	
	public List<List<String>> identifyPPRFeatureCandidates(String modelsFileName) {
		URL url = ClassLoader.getSystemResource(modelsFileName);
		
		readModels(url);

//		printConstructionPrimitiveCodes();

//		printConustructionPrimitivesInModels();

		List<ImmutableModelPrimitiveTuple> tuples = createTuples();

//		printTuples(tuples);
		
		
		List<List<String>> bigF = new ArrayList<>();
		while (!tuples.isEmpty()) {
			// Start iteration
			
			String mfcp = ImmutableModelPrimitiveTuple.mostFrequentConstructionPrimitive(tuples);
			
//			System.out.println("MFCP");
//			System.out.println(mfcp);
			
			List<String> products = new ArrayList<>();
			for (String modelNumber : modelsWithConstructionPrimitives.keySet()) {
				List<String> constructionPrimitives = modelsWithConstructionPrimitives.get(modelNumber);
				if (constructionPrimitives.contains(mfcp)) {
					products.add(modelNumber);
				}
			}
			
//			System.out.println("Products");
//			products.forEach(p -> System.out.println(p));
			
			List<String> f = ImmutableModelPrimitiveTuple.toListOfConstructionPrimitives(tuples).stream().distinct().collect(Collectors.toList());;
			if (products.size() == 1) {
				f.retainAll(modelsWithConstructionPrimitives.get(products.get(0)));
			} else {
				for (String product : products) {
					f.retainAll(modelsWithConstructionPrimitives.get(product));
				}
			}
			
//			System.out.println("f");
//			f.forEach(cp -> System.out.println(String.format("%s - %s", cp, constructionPrimitiveCodes.get(cp))));
			
			for (String singleF : f) {
				tuples.removeIf(tuple -> tuple.getConstructionPrimitive().equals(singleF));
			}
						
//			System.out.println("f");
//			tuples.forEach(tuple -> System.out.println(constructionPrimitiveCodes.get(tuple.getConstructionPrimitive())));
			
			bigF.add(f);
			// End iteration			
		}
		
		System.out.println(bigF);
		return bigF;
	}

	private void printTuples(List<ImmutableModelPrimitiveTuple> tuples) {
		System.out.println("Tuples");
		ImmutableModelPrimitiveTuple.order(tuples);
		for (ImmutableModelPrimitiveTuple tuple : tuples) {
			System.out.println(tuple);
		}
	}

	private List<ImmutableModelPrimitiveTuple> createTuples() {
		List<ImmutableModelPrimitiveTuple> tuples = new ArrayList<>();
		for (String constructionPrimitive : constructionPrimitiveInModels.keySet()) {
			List<String> models = constructionPrimitiveInModels.get(constructionPrimitive);

			for (String model : models) {
				tuples.add(new ImmutableModelPrimitiveTuple(model, constructionPrimitive));
			}
		}
		return tuples;
	}

	private void printConustructionPrimitivesInModels() {
		System.out.println("Constructionmodels");
		constructionPrimitiveInModels.keySet().forEach(constructionPrimitive -> System.out.println(String
				.format("%s - %s", constructionPrimitiveInModels.get(constructionPrimitive), constructionPrimitive)));
	}

	private void printConstructionPrimitiveCodes() {
		System.out.println("Constructionprimitives");
		constructionPrimitiveCodes.keySet().forEach(constructionPrimitive -> System.out.println(String.format("%s - %s",
				constructionPrimitiveCodes.get(constructionPrimitive), constructionPrimitive)));
	}

	private List<String> readFile(URL fileName) {
		List<String> lines = new ArrayList<>();
		try {
			lines = Files.readAllLines(Paths.get(fileName.toURI()), Charset.forName("UTF-8"));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	private void readModels(URL fileName) {
		char constructionPrimitiveCode = 'a';
		List<String> modelLines = readFile(fileName);

		for (String line : modelLines) {
			String[] splitLine = line.split("=");
			String modelNumber = splitLine[0];
			String construcionPrimitive = splitLine[1];

			if (modelsWithConstructionPrimitives.containsKey(modelNumber)) {
				List<String> constructionPrimitives = modelsWithConstructionPrimitives.get(modelNumber);
				constructionPrimitives.add(construcionPrimitive);
			} else {
				List<String> constructionPrimitives = new ArrayList<String>();
				constructionPrimitives.add(construcionPrimitive);
				modelsWithConstructionPrimitives.put(modelNumber, constructionPrimitives);
			}

			if (constructionPrimitiveInModels.containsKey(construcionPrimitive)) {
				List<String> models = constructionPrimitiveInModels.get(construcionPrimitive);
				if (!models.contains(modelNumber)) {
					models.add(modelNumber);
				}
			} else {
				List<String> models = new ArrayList<String>();
				models.add(modelNumber);
				constructionPrimitiveInModels.put(construcionPrimitive, models);
			}

			if (!constructionPrimitiveCodes.containsKey(construcionPrimitive)) {
				constructionPrimitiveCodes.put(construcionPrimitive, String.valueOf(constructionPrimitiveCode++));
			}
		}
	}
}
