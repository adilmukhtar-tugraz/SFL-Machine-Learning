package at.tugraz.ist.debugging.modelbased.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import at.tugraz.ist.debugging.modelbased.Diagnosis;
import at.tugraz.ist.debugging.modelbased.EDebuggingAlgorithm;
import at.tugraz.ist.debugging.modelbased.EModelGranularity;
import at.tugraz.ist.debugging.modelbased.ESolver;
import at.tugraz.ist.debugging.modelbased.ESolverAccessOption;
import at.tugraz.ist.debugging.modelbased.ModelBasedResult;
import at.tugraz.ist.debugging.modelbased.Strategy;
import at.tugraz.ist.debugging.modelbased.minion.MinionConstraintStrategy;
import at.tugraz.ist.debugging.modelbased.solver.ConstraintStrategy;
import at.tugraz.ist.debugging.modelbased.solver.ConstraintStrategyConfiguration;
import at.tugraz.ist.debugging.spreadsheets.configuration.SpreadsheetProperties;
import at.tugraz.ist.debugging.spreadsheets.datastructures.Coords;
import at.tugraz.ist.util.RuntimeProcessExecuter;
import at.tugraz.ist.util.debugging.Writer;
import at.tugraz.ist.util.fileManipulation.Directory;
import at.tugraz.ist.util.fileManipulation.FileTools;
import at.tugraz.ist.util.time.TimeSpan;
import at.tugraz.ist.util.time.TimeSpan.Precision;

public class MinionFileCreator {

	public static int RUNS = 1;
	private static int timeout = 20*60;//20 * 60;
	public static BufferedWriter writer = null;
	public static boolean writeHeader = false;

	public static void main(String[] args) {
		try {
			File logFile = new File("Comparison.log");
			if (!logFile.exists())
				logFile.createNewFile();
			PrintStream log = new PrintStream(logFile);
						System.setOut(log);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		Writer.setActive(false);
		List<String> files = new ArrayList<String>();
//		 files.addAll(Directory.getFiles("Benchmarks\\Iulia\\SumCircPropertyFiles",
//		 files.addAll(Directory.getFiles("Benchmarks\\Iulia\\SEEDED\\PropertiesFiles",
//		 ".properties"));
		
		files.add("Benchmarks\\test\\spreadsheet_ex.properties");
//		files.add("Benchmarks\\Iulia\\SEEDED\\PropertiesFiles\\circuit20_20_2FAULTS_FAULTVERSION4.properties");
//		files.add("Benchmarks\\Iulia\\SEEDED\\PropertiesFiles\\circuit2_20_2FAULTS_FAULTVERSION4.properties");
//		files.add("Benchmarks\\Iulia\\SEEDED\\PropertiesFiles\\circuit2_2_3FAULTS_FAULTVERSION2.properties");
//		files.add("Benchmarks\\Iulia\\SEEDED\\PropertiesFiles\\circuit2_3_3FAULTS_FAULTVERSION1.properties");
//		files.add("Benchmarks\\QR-Example.properties");

//		files.addAll(Directory.getFiles("Benchmarks", ".properties"));

		// files.add("Benchmarks\\INTEGER\\configuration_files\\fromAFW\\AFW_arithmetics02_1Faults_Fault1.properties");
		// files.add("Benchmarks\\test\\adder.properties");
		String directoryName = System.getProperty("user.dir");

		try {
			String csvFileName = "artificialSpreadsheets.csv";
			if (!new File(csvFileName).exists())
				writeHeader = true;
			writer = new BufferedWriter(new FileWriter(csvFileName, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String file : files) {
			if (skip(file)) {
				continue;
			}

			String pFile = null;
			if (file.contains(directoryName)) {
				pFile = file;
			} else {
				pFile = directoryName + System.getProperty("file.separator") + file;
			}
			String minionOut = pFile.replace(".properties", ".minion");
			minionOut = minionOut.substring(pFile.lastIndexOf(System.getProperty("file.separator")) + 1);
			minionOut = System.getProperty("user.dir") + System.getProperty("file.separator") + "minionFiles"
					+ System.getProperty("file.separator") + minionOut;
			createMinionFile(pFile, minionOut);
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Boolean skip(String file) {	
		
//		if(file.contains("circuit20_15_3FAULTS_FAULTVERSION2"))
			return false;
//		return true;
		
//		if(file.contains("circuit2_2"))
//			return false;
//		return true;
	
//		if(file.contains("arithmetics01_2Faults_Fault3")||file.contains("arithmetics02_1Faults_Fault2")||file.contains("arithmetics02_2Faults_Fault2")||file.contains("arithmetics02_2Faults_Fault3")||file.contains("cake")||file.contains("fibonacci")||file.contains("shares_2"))
//			return false;
//		else
//			return true;
		
		
//		if (file.contains("AFW_oscars2012_1Faults_Fault1") || file.contains("AFW_oscars2012_1Faults_Fault2"))
//			return true;
//
//		if (file.contains("AFW_arithmetics03") || file.contains("AFW_arithmetics04"))
//			return true;
//
//		// temporary skip
//		if (file.contains("AFW_energy_"))
//			return true;
//
//		// containing IF
//		if (file.contains("FW_amortization_") || file.contains("AFW_area_") || file.contains("AFW_austrian_league_")
//				|| file.contains("AFW_bank_account_") || file.contains("AFW_birthdays_")
//				|| file.contains("AFW_book_recommendation_") || file.contains("AFW_computer_shopping_")
//				|| file.contains("AFW_conditionals") || file.contains("AFW_dice_rolling_")
//				|| file.contains("AFW_euclidean_algorithm_") || file.contains("AFW_matrix_")
//				|| file.contains("AFW_ranking_") || file.contains("AFW_shopping_bedroom2_")
//				|| file.contains("AFW_training_") || file.contains("AFW_weather_")
//				|| file.contains("AFW_wimbledon2012_"))
//			return true;
//
//		// max function not implemented
//		if (file.contains("AFW_parabola_"))
//			return true;
//
//		// // multiplication with 0
//		// if(file.contains("AFW_cake")||file.contains("AFW_shopping_bedroom1")||file.contains("AFW_shares_"))
//		// return true;
//		//
//		// // negative numbers
//		// if(file.contains("AFW_prom_calculator_"))
//		// return true;
//
//		return false;
	}

	

	public static void createMinionFile(String propertiesFile, String outFile) {
		try {
			SpreadsheetProperties properties = new SpreadsheetProperties(propertiesFile);
//			if (properties.getFaultyCells().size() > 1)
//				return;

			System.out.println(propertiesFile.substring(39));
			System.out.println("Value-based model");
			ModelBasedResult valueResult = null;
			valueResult = computeResult(properties, EModelGranularity.Value);
			rename(outFile, "_value");
			
			System.out.println("Comparison-based model");
			ModelBasedResult comparisionResult = computeResult(properties, EModelGranularity.Comparison);
			rename(outFile, "_comparison");
			

			System.out.println("Dependency-based model");
			ModelBasedResult sophDepResult = computeResult(properties, EModelGranularity.Sophisticated);
			rename(outFile, "_dependency");
			
			List<Diagnosis> trueFault = new ArrayList<Diagnosis>();
			List<Coords> faults = properties.getFaultyCells();
			Diagnosis diag = new Diagnosis();
			diag.addAll(faults);
			trueFault.add(diag);
			

			ComparisionModelResult cmr = new ComparisionModelResult(
					propertiesFile.substring(39).replace(".properties", ""),
					valueResult.getAllDiagnoses(), comparisionResult.getAllDiagnoses(), sophDepResult.getAllDiagnoses(), trueFault,
					valueResult.getRuntimeSolvingDiagGranularity(), comparisionResult.getRuntimeSolvingDiagGranularity(),
					sophDepResult.getRuntimeSolvingDiagGranularity(), properties, ConstraintStrategyConfiguration.useEarlyTermination(),ConstraintStrategyConfiguration.getMaxDiagnosesSize() );

			if (writeHeader) {
				writer.write(ComparisionModelResult.getColumnHeader());
				writeHeader = false;
			}

			writer.write(cmr.toString());
			writer.flush();


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void rename(String file, String modelname){
		try{
			FileTools.renameFile(
					System.getProperty("user.dir") + System.getProperty("file.separator")
							+ MinionConstraintStrategy.getMinionFileName(),
					file.replace(".minion", modelname+".minion"), true);
			}catch(IOException ex){
				System.out.println("########## 2nd try renaming file ############");
				System.out.println(ex.getMessage());
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					FileTools.renameFile(
							System.getProperty("user.dir") + System.getProperty("file.separator")
									+ MinionConstraintStrategy.getMinionFileName(),
							file.replace(".minion", modelname+".minion"), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

	public static ModelBasedResult computeResult(SpreadsheetProperties properties, EModelGranularity modelGranularity) {
		List<Map<Integer, TimeSpan>> allTimes = new ArrayList<Map<Integer, TimeSpan>>();
//		TimeSpan totaltime = new TimeSpan(Precision.MILLISECONDS);
		ModelBasedResult result = null;
		Strategy strategy = new Strategy(ESolver.Minion, EDebuggingAlgorithm.ConstraintBased, ESolverAccessOption.API,
				modelGranularity);
		try {

			ConstraintStrategyConfiguration.setStrategy(strategy);
			ConstraintStrategyConfiguration.setUseCones(false);
			ConstraintStrategyConfiguration.setEarlyTermination(false);
			ConstraintStrategyConfiguration.setRuns(RUNS);
			ConstraintStrategyConfiguration.setUseStrings(false);
			ConstraintStrategyConfiguration.setVerifySolution(false);
			ConstraintStrategyConfiguration.setMaxDiagnosesSize(3);

			// String directory =
			// System.getProperty("user.dir")+System.getProperty("file.separator")+"Benchmarks\\INTEGER\\";

			String directory = System.getProperty("user.dir") + System.getProperty("file.separator")+ "Benchmarks\\";
			
			if(properties.getExcelSheetName().contains("AFW"))
				directory +=  "INTEGER\\";
					
			
			if(properties.getPropertyFileName().contains("SumCircPropertyFiles"))
				directory += "Iulia\\SumCircExcelFiles\\";

			int retries = 3;
			for (int r = 1; r <= ConstraintStrategyConfiguration.getRuns(); ++r) {
				Executor executor = new Executor(directory, properties);
				try {
					executor.start();
				} catch (RuntimeException e) {
					if (retries-- == 0)
						throw e;
					--r;
					continue;
				}
				if (timeout <= 0)
					executor.join();
				else
					executor.join(timeout * 1000);

				result = executor.getResult();
				if (executor.isAlive()) {
					System.out.println(properties.getExcelSheetName() + " ... timeout!");
					RuntimeProcessExecuter.killProcess(ConstraintStrategy.externalProcess);
				}
				
				if (result == null) {
					System.out.println(properties.getExcelSheetName() + " " + strategy.getName() + " failed: "
							+ executor.getErrorMessage() + System.lineSeparator());
					result = new ModelBasedResult(properties.getExcelSheetName(), strategy,
							ConstraintStrategyConfiguration.useStrings(), -1, -1);
					result.setRuntime(-1);
					break;
				} 
				if (executor.getResult() != null) {
					allTimes.add(executor.getResult().getRuntimeSolvingDiagGranularity());
//					totaltime.add(executor.getResult().getRuntimeSolving());
//					System.out.println("Something STRANGE happened!");
					
				}

				if (r < ConstraintStrategyConfiguration.getRuns()) {
					continue;
				}
				if (EvaluationGUI.checkDiagnosis(result.getAllDiagnoses(), properties.getFaultyCells()) == true) {
					result.setDiagnosisContained(true);
				} else {
					result.setDiagnosisContained(false);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<Integer, TimeSpan> avgTime = new HashMap<Integer, TimeSpan>();
		for(Map<Integer, TimeSpan> time : allTimes){
			for(int i : time.keySet()){
				TimeSpan t = new TimeSpan(Precision.MICROSECONDS);
				if(avgTime.containsKey(i)){
					t = avgTime.get(i);
				}
				t.add(time.get(i));
				avgTime.put(i, t);
			}
			
		}
		for(int i: avgTime.keySet()){
			TimeSpan t = avgTime.get(i);
			t.divide(ConstraintStrategyConfiguration.getRuns());
			avgTime.put(i, t);
		}
		
		result.addSolvingTimes(avgTime);

		return result;

	}

}
