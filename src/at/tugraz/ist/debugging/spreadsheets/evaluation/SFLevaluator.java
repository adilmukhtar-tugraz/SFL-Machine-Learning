package at.tugraz.ist.debugging.spreadsheets.evaluation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.*;
import com.*;

import java.util.*;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.reflections.Reflections;

import at.tugraz.ist.debugging.spectrumbased.similaritycoefficients.SimilarityCoefficient;
import at.tugraz.ist.debugging.spectrumbased.similaritycoefficients.SimilarityCoefficient.CoefficientComparator;
import at.tugraz.ist.debugging.spreadsheets.algorithms.SpectrumBasedResult;
import at.tugraz.ist.debugging.spreadsheets.algorithms.spectrum.SFL;
/*import at.tugraz.ist.debugging.spreadsheets.algorithms.spectrum.SHSC;
import at.tugraz.ist.debugging.spreadsheets.algorithms.spectrum.Sendys;*/
import at.tugraz.ist.debugging.spreadsheets.configuration.SpreadsheetProperties;
import at.tugraz.ist.debugging.spreadsheets.configuration.SpreadsheetPropertiesException;
import at.tugraz.ist.debugging.spreadsheets.configuration.algorithm.SpectrumConfig;
import at.tugraz.ist.debugging.spreadsheets.datastructures.Coords;
import at.tugraz.ist.debugging.modelbased.CellContainer;
import at.tugraz.ist.debugging.spreadsheets.evaluation.ranking.IRanking;
import at.tugraz.ist.util.IO.OutputConfigurator;
import at.tugraz.ist.util.fileManipulation.Directory;


public class SFLevaluator {

	private static final String COEFFICIENTS_PACKAGE = "at.tugraz.ist.debugging.spectrumbased.similaritycoefficients";

	private static List<String> files = new ArrayList<String>();
	public static String PATH = "Benchmarks\\EUSES_Spreadsheets" + File.separator;
	
	public static PermanantResultStorage resultStorage;
	
	public boolean init_coefficients = false;
	
	public static void main(String[] args) {
		
		//System.out.println(PATH);
		//OutputConfigurator.setOutputAndErrorStreamToFile("Results.log");
		//System.out.println("output configurator...");
		SFLevaluator debug = new SFLevaluator();
		Gson gson = new Gson();
		files = Directory.getFilesRecursively(PATH, ".properties");
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd hh_mm");
		resultStorage = new PermanantResultStorage("results_" + ft.format(now)
				+ ".csv");

		List<SimilarityCoefficient> coefficients = new ArrayList<SimilarityCoefficient>();
		Reflections reflections = new Reflections(COEFFICIENTS_PACKAGE);
		for (Class<? extends SimilarityCoefficient> sc : reflections
				.getSubTypesOf(SimilarityCoefficient.class)) {
			try {
				
				coefficients.add(sc.newInstance());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		Collections.sort(coefficients, new CoefficientComparator());
		
		int fileCounter = 0;
		
		Map<String, List<Double>> jsonCoefficients = new HashMap<>();
		jsonCoefficients.put("Faulty", new ArrayList<Double>());
		
		for (String file : files) {
			
			
			//FileWriter fileJson = null;

			try {
				boolean faults_added = false;
				
				jsonCoefficients = debug.debug(file, coefficients, jsonCoefficients, faults_added);
				System.out.println(fileCounter);
				
			} catch (Exception e) {
				System.err.println("Exception in file " + file);
				System.err.println(e.toString());
			}
			try {
				Thread.sleep(5000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		fileCounter += 1;
		}
		JSONObject json =  new JSONObject(jsonCoefficients);
		try (FileWriter file = new FileWriter("C:\\Users\\Adil Mukhtar\\Desktop\\SFLspreadsheets-master\\Json Files for ML\\data.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(json.toString()); 
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
		
    
	}

	boolean debug = false;

	public Map<String, List<Double>> debug(String configFile,
			List<SimilarityCoefficient> coefficients, Map<String, List<Double>> jsonCoefficients, boolean faults_added) {

		SpreadsheetProperties config = null;

		try {
			config = new SpreadsheetProperties(configFile);
			CellContainer cells = CellContainer.create(PATH
					+ config.getExcelSheetName()); // TODO: change to getPath
			System.out.println(config.getExcelSheetName());
			//System.out.println(cells.getCells().size());
			SpectrumConfig spectrumData = new SpectrumConfig(config, cells);
			
			
			spectrumData.setDebug(debug);
			//how does it look like.
			
			//System.out.println(config.getFaultyCells());
			
			for (SimilarityCoefficient sc : coefficients) {
				 //System.out.println(sc.getCoefficientName());
				 if (!init_coefficients) {
					 jsonCoefficients.put(sc.getCoefficientName(), new ArrayList<Double>());
				 }
				 SFL scObj = new SFL(sc);
				 IRanking<Coords> ranking = scObj.runAlgorithm(
				 spectrumData).getRanking();
				 
				 //System.out.println(scObj.cellToCoefficientMapping());
				 List<Double> listCellsCoefficients = new ArrayList<Double>();
				 List<Double> faults = new ArrayList<Double>();
				 
				 
				 for (Entry<Coords, Double> entry :scObj.cellToCoefficientMapping().entrySet()) { 
					 if(!faults_added) {
						 if (config.getFaultyCells().contains(entry.getKey())) {
							 faults = jsonCoefficients.get("Faulty");
							 faults.add(1.0);
							 jsonCoefficients.put("Faulty", faults);
						 }
						 else {
							 faults = jsonCoefficients.get("Faulty");
							 faults.add(0.0);
							 jsonCoefficients.put("Faulty", faults);
						 }
						 
					 }
					 listCellsCoefficients = jsonCoefficients.get(sc.getCoefficientName());
					 listCellsCoefficients.add(entry.getValue());
					 jsonCoefficients.put(sc.getCoefficientName(), listCellsCoefficients);
					 
				 }
				 
				 faults_added = true;

			}
			init_coefficients = true;

			/*
			 * res.setSpectrumBasedAlgo("SHSC", new
			 * SHSC().runAlgorithm(spectrumData).getRanking());
			 * res.setSpectrumBasedAlgo("Sendys", new
			 * Sendys().runAlgorithm(spectrumData).getRanking());
			 */

		
			
			

		} catch (SpreadsheetPropertiesException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonCoefficients;
	}
	static public void CrunchifyLog(String str) {
        System.out.println("str");
    }
}
