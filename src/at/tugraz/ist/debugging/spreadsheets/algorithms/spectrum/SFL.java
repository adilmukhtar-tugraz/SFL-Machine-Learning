package at.tugraz.ist.debugging.spreadsheets.algorithms.spectrum;

import java.util.Map;

import at.tugraz.ist.debugging.spectrumbased.similaritycoefficients.SimilarityCoefficient;
import at.tugraz.ist.debugging.spreadsheets.algorithms.SpectrumBasedAlgorithm;
import at.tugraz.ist.debugging.spreadsheets.datastructures.Coords;
import at.tugraz.ist.debugging.spreadsheets.evaluation.ranking.IRanking;
import at.tugraz.ist.debugging.spreadsheets.evaluation.ranking.Ranking;

public class SFL extends SpectrumBasedAlgorithm {

	private SimilarityCoefficient sc;
	private Map<Coords, Double> cellToCoefficientMapping;

	public SFL(SimilarityCoefficient sc) {
		this.sc = sc;
	}
	
	public SFL(SimilarityCoefficient sc, boolean dynamic) {
		this.sc = sc;
		this.dynamic = dynamic;
	}

	public SimilarityCoefficient getSC() {
		return sc;
	}

	@Override
	protected IRanking<Coords> runAlgorithm() {
		
		this.cellToCoefficientMapping = data.getObservationMatrix().getCoefficientValues(sc);
		return new Ranking<Coords>(data.getObservationMatrix()
				.getCoefficientValues(sc));
	}
	
	public Map<Coords, Double> cellToCoefficientMapping(){
		return this.cellToCoefficientMapping;
	}
	@Override
	public String getName() {
		
		String str = super.getName();
		
		str += " (" + sc.getCoefficientName();
	
		if (dynamic != null)
			str += ", " +((dynamic)?"dyn":"s");
		
		str += ")";

		return str;
	}

}
