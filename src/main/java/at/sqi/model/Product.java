package at.sqi.model;

public class Product {
	private String inputProductName;
	private String outProductName;

	public Product(String inputProductName, String outProductName) {
		this.inputProductName = inputProductName;
		this.outProductName = outProductName;
	}

	public String getInputProductName() {
		return inputProductName;
	}

	public String getOutProductName() {
		return outProductName;
	}
}
