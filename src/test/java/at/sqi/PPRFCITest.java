package at.sqi;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class PPRFCITest {

	@Test
	public void testWithCP1_expected() {
		FCI featureIdentificationCandidate = new FCI();
		List<List<String>> fc = featureIdentificationCandidate
				.identifyPPRFeatureCandidates("constructionprimitives1.txt");
		SIModel model = new SIModel();
		model.buildModel(fc);
		assertTrue(true);
	}

	@Test
	public void testWithCP2_expected() {
		FCI featureIdentificationCandidate = new FCI();
		List<List<String>> fc = featureIdentificationCandidate
				.identifyPPRFeatureCandidates("constructionprimitives2.txt");
		SIModel model = new SIModel();
		model.buildModel(fc);
		assertTrue(true);
	}

	@Test
	public void testWithCP3_expected() {
		FCI featureIdentificationCandidate = new FCI();
		List<List<String>> fc = featureIdentificationCandidate
				.identifyPPRFeatureCandidates("constructionprimitives3.txt");
		SIModel model = new SIModel();
		model.buildModel(fc);
		assertTrue(true);
	}

	@Test
	public void testWithCP4_expected() {
		FCI featureIdentificationCandidate = new FCI();
		List<List<String>> fc = featureIdentificationCandidate.identifyPPRFeatureCandidates("rockerswitch-12var.txt");
		assertTrue(true);
	}
}
