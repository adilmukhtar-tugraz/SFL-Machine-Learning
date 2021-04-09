import at.tugraz.ist.debugging.spreadsheets.configuration.SpreadsheetPropertiesException;
import at.tugraz.ist.debugging.modelbased.CellContainer;
import at.tugraz.ist.debugging.spreadsheets.util.POIReader;
import at.tugraz.ist.debugging.spreadsheets.algorithms.SpectrumBasedAlgorithm;
import at.tugraz.ist.debugging.spreadsheets.configuration.algorithm.SpreadsheetInput;
import at.tugraz.ist.debugging.spreadsheets.configuration.SpreadsheetProperties;
import at.tugraz.ist.debugging.spreadsheets.algorithms.spectrum.SFL;
import at.tugraz.ist.debugging.spectrumbased.similaritycoefficients.SimilarityCoefficient;

public class SFLMain {
	public static void main(String[] args) throws SpreadsheetPropertiesException {
		POIReader poi = null;
		CellContainer cellContainer = null;
		POIReader poiReader = poi.create("C:\\Users\\Adil Mukhtar\\Desktop\\test.xlsx", cellContainer);
		System.out.println(poiReader);
		SpreadsheetProperties props = new SpreadsheetProperties();
		props.generate("C:/Users/Adil Mukhtar/Desktop/test.txt");
		
		
	}

}
