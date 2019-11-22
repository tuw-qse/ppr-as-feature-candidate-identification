package at.sqi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Represents a two element tuple connecting the model and a construction
 * primitive.
 */
public class ImmutableModelPrimitiveTuple {
	private String model;
	private String constructionPrimitive;

	public ImmutableModelPrimitiveTuple(String model, String constructionPrimitive) {
		this.model = model;
		this.constructionPrimitive = constructionPrimitive;
	}

	public String getModel() {
		return model;
	}

	public String getConstructionPrimitive() {
		return constructionPrimitive;
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", model, constructionPrimitive);
	}

	public static void order(List<ImmutableModelPrimitiveTuple> tuples) {
		Collections.sort(tuples, new Comparator<ImmutableModelPrimitiveTuple>() {
			@Override
			public int compare(ImmutableModelPrimitiveTuple o1, ImmutableModelPrimitiveTuple o2) {
				int modelCompare = o1.getModel().compareTo(o2.getModel());
				if (modelCompare != 0) {
					return modelCompare;
				}
				int constructionPrimitiveCompare = o1.getConstructionPrimitive()
						.compareTo(o2.getConstructionPrimitive());
				return constructionPrimitiveCompare;
			}
		});
	}

	public static int constructionPrimitiveFrequency(List<ImmutableModelPrimitiveTuple> tuples,
			String constructionPrimitive) {
		int count = 0;

		for (ImmutableModelPrimitiveTuple tuple : tuples) {
			if (tuple.getConstructionPrimitive().equals(constructionPrimitive)) {
				count++;
			}
		}

		return count;
	}

	public static String mostFrequentConstructionPrimitive(List<ImmutableModelPrimitiveTuple> tuples) {
		HashMap<String, Integer> frequencies = new HashMap<>();

		for (ImmutableModelPrimitiveTuple tuple : tuples) {
			String constructionPrimitive = tuple.getConstructionPrimitive();
			if (frequencies.containsKey(constructionPrimitive)) {
				int frequency = frequencies.get(constructionPrimitive);
				frequency++;
				frequencies.put(constructionPrimitive, frequency);
			} else {
				frequencies.put(constructionPrimitive, 1);
			}
		}

		int maxCount = 0;
		String result = "";

		for (Entry<String, Integer> entry : frequencies.entrySet()) {
			if (maxCount < entry.getValue()) {
				result = entry.getKey();
				maxCount = entry.getValue();
			}
		}

		return result;
	}
	
	public static List<String> toListOfConstructionPrimitives(List<ImmutableModelPrimitiveTuple> tuples) {
		List<String> result = new ArrayList<>();
		
		tuples.forEach(tuple -> result.add(tuple.getConstructionPrimitive()));
		
		return result;
	}
}
