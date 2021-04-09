package at.tugraz.ist.debugging.modelbased.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.tugraz.ist.debugging.modelbased.Diagnosis;
import at.tugraz.ist.debugging.spreadsheets.configuration.SpreadsheetProperties;
import at.tugraz.ist.debugging.spreadsheets.datastructures.Coords;
import at.tugraz.ist.debugging.spreadsheets.evaluation.Result;
import at.tugraz.ist.util.time.TimeSpan;
import at.tugraz.ist.util.time.TimeSpan.Precision;

public class ComparisionModelResult extends Result {


	private Map<Integer, Boolean> getTimeoutMap(Map<Integer, TimeSpan> times, boolean earlyTermination,
			int maxDiagSize) {
		Map<Integer, Boolean> timeoutMap = new HashMap<Integer, Boolean>();
		for (int i = 1; i <= maxDiagSize; i++) {
			if (times.containsKey(i) || (earlyTermination && i != 1))
				timeoutMap.put(i, false);
			else
				timeoutMap.put(i, true);
		}
		return timeoutMap;
	}

	public ComparisionModelResult(String propertiesFile, List<Diagnosis> valueBasedDiagnoses,
			List<Diagnosis> comparisonBasedDiagnoses, List<Diagnosis> sophisticatedDependencyDiagnoses, List<Diagnosis> trueFault,
			Map<Integer, TimeSpan> valueBasedTime, Map<Integer, TimeSpan> comparisonBasedTime,
			Map<Integer, TimeSpan> sophisticatedDependencyBasedTime, SpreadsheetProperties properties,
			boolean earlyTermination, int maxDiagSize) {
		super();
		super.clear();
		addData("Properties file", propertiesFile);

		Map<Integer, Boolean> timeoutMapVBM = getTimeoutMap(valueBasedTime, earlyTermination, maxDiagSize);
		Map<Integer, Boolean> timeoutMapDBM = getTimeoutMap(sophisticatedDependencyBasedTime, earlyTermination,
				maxDiagSize);
		Map<Integer, Boolean> timeoutMapCBM = getTimeoutMap(comparisonBasedTime, earlyTermination, maxDiagSize);

		addDiagnosisData("VBM", valueBasedDiagnoses, timeoutMapVBM, maxDiagSize);
		addDiagnosisData("DBM", sophisticatedDependencyDiagnoses, timeoutMapDBM, maxDiagSize);
		addDiagnosisData("CBM", comparisonBasedDiagnoses, timeoutMapCBM, maxDiagSize);

		addData("diag VBM", valueBasedDiagnoses.size());
		addData("diag DBM", sophisticatedDependencyDiagnoses.size());
		addData("diag CBM", comparisonBasedDiagnoses.size());
		
		addData("All VBM containd in DBM", areAllDiagnosesContained(sophisticatedDependencyDiagnoses,
				valueBasedDiagnoses, timeoutMapDBM, timeoutMapVBM) ? "true" : "false");		
		addData("All VBM containd in CBM",
				areAllDiagnosesContained(comparisonBasedDiagnoses, valueBasedDiagnoses, timeoutMapCBM, timeoutMapVBM)
						? "true" : "false");
		
		addData("True fault in VBM", areAllDiagnosesContained(valueBasedDiagnoses, trueFault, null, null) ? "true" : "false");
		addData("True fault in DBM", areAllDiagnosesContained(sophisticatedDependencyDiagnoses, trueFault, null, null) ? "true" : "false");
		addData("True fault in CBM", areAllDiagnosesContained(comparisonBasedDiagnoses, trueFault, null, null) ? "true" : "false");

		
		
		for (int i = 1; i <= maxDiagSize; i++) {
			if (valueBasedTime.containsKey(i)) {
				addData("VBM time [ms] (diagnosis size=" + i + ")",
						valueBasedTime.get(i).toString(Precision.MILLISECONDS).replace("ms", ""));
			} else if (!earlyTermination || i == 1) {
				addData("VBM time [ms] (diagnosis size=" + i + ")", "timeout");
			} else {
				addData("VBM time [ms] (diagnosis size=" + i + ")", "-");
			}
		}


		for (int i = 1; i <= maxDiagSize; i++) {
			if (sophisticatedDependencyBasedTime.containsKey(i)) {
				addData("DBM time [ms] (diagnosis size=" + i + ")",
						sophisticatedDependencyBasedTime.get(i).toString(Precision.MILLISECONDS).replace("ms", ""));
			} else if (!earlyTermination || i == 1) {
				addData("DBM time [ms] (diagnosis size=" + i + ")", "timeout");
			} else {
				addData("DBM time [ms] (diagnosis size=" + i + ")", "-");
			}
		}

		
		for (int i = 1; i <= maxDiagSize; i++) {
			if (comparisonBasedTime.containsKey(i)) {
				addData("CBM time [ms] (diagnosis size=" + i + ")",
						comparisonBasedTime.get(i).toString(Precision.MILLISECONDS).replace("ms", ""));
			} else if (!earlyTermination || i == 1) {
				addData("CBM time [ms] (diagnosis size=" + i + ")", "timeout");
			} else {
				addData("CBM time [ms] (diagnosis size=" + i + ")", "-");
			}
		}
		
	

	
		
		addData("Incorrect Output cells", properties.getIncorrectOutputCells().size());
		addData("Correct Output cells", properties.getCorrectOutputCells().size());
		addData("Faulty cells", properties.getFaultyCells().size());
	}

	private void addDiagnosisData(String modelName, List<Diagnosis> diagnoses, Map<Integer, Boolean> timeoutMap,
			int maxDiagSize) {
		Map<Integer, List<Diagnosis>> diagnosesBySize = getDiagnosesBySize(diagnoses);
		for (int i = 1; i <= maxDiagSize; i++) {
			if (diagnosesBySize.containsKey(i)) {
				addData(modelName + " diagnosis size=" + i, diagnosisToString(diagnosesBySize.get(i)));
				addData(modelName + " diagnosis amount (size=" + i + ")", diagnosesBySize.get(i).size());
			} else if (timeoutMap.get(i) == true) {
				addData(modelName + " diagnosis size=" + i, "timeout");
				addData(modelName + " diagnosis amount (size=" + i + ")", "timeout");
			} else {
				addData(modelName + " diagnosis size=" + i, "-");
				addData(modelName + " diagnosis amount (size=" + i + ")", 0);
			}
		}
	}

	private Map<Integer, List<Diagnosis>> getDiagnosesBySize(List<Diagnosis> diagnoses) {
		Map<Integer, List<Diagnosis>> diagnosesBySize = new HashMap<Integer, List<Diagnosis>>();
		for (Diagnosis diag : diagnoses) {
			int size = diag.size();
			List<Diagnosis> sameSizeDiag = new ArrayList<Diagnosis>();
			if (diagnosesBySize.containsKey(size))
				sameSizeDiag = diagnosesBySize.get(size);
			sameSizeDiag.add(diag);
			diagnosesBySize.put(size, sameSizeDiag);
		}
		return diagnosesBySize;
	}

	private boolean areAllDiagnosesContained(List<Diagnosis> superset, List<Diagnosis> subset,
			Map<Integer, Boolean> timeoutSuperset, Map<Integer, Boolean> timeoutSubset) {

		Boolean timeoutHappened = false;

		if (timeoutSuperset != null) {
			for (Boolean timeout : timeoutSuperset.values()) {
				if (timeout)
					timeoutHappened = true;
				break;
			}
		}

		if (timeoutSubset != null && timeoutHappened == false) {
			for (Boolean timeout : timeoutSubset.values()) {
				if (timeout)
					timeoutHappened = true;
				break;
			}
		}

		for (Diagnosis diagnosis : subset) {
			if (superset.contains(diagnosis))
				continue;

			boolean contained = false;
			for (Diagnosis diag : superset) {
				if (isSubDiagnosis(diagnosis, diag)) {
					contained = true;
					break;
				}
			}
			if (!contained) {
				if (!timeoutHappened) {
					System.out.println(super.getResultEntry("Properties file") + ": Diagnosis " + diagnosis.toString()
							+ " not contained!");
				}
				return false;
			}
		}
		return true;
	}

	private String diagnosisToString(List<Diagnosis> diagnoses) {
		StringBuilder strB = new StringBuilder();
		for (Diagnosis diagnosis : diagnoses) {
			strB.append("(");
			for (Coords cell : diagnosis.getCells()) {
				strB.append(cell.getConstraintString());
				strB.append(",");
			}
			strB.append("),");
		}
		String str = strB.toString().replace(",)", (")"));
		if (str.length() > 0)
			return str.substring(0, str.length() - 1);
		return "";
	}

	private Integer getNumberOfCellsInDiagnoses(List<Diagnosis> diagnoses) {
		Set<Coords> cells = new HashSet<Coords>();
		for (Diagnosis diagnosis : diagnoses) {
			cells.addAll(diagnosis.getCells());
		}
		return cells.size();
	}

	private boolean isSubDiagnosis(Diagnosis superDiagnosis, Diagnosis subDiagnosis) {
		for (Coords subCell : subDiagnosis.getCells()) {
			if (!superDiagnosis.getCells().contains(subCell))
				return false;
			/*
			 * boolean contained = false; for (Coords superCell :
			 * superDiagnosis.getCells()) { if
			 * (subCell.toString().equalsIgnoreCase(superCell.toString())) {
			 * contained = true; break; } } if (!contained) { return false; }
			 */
		}
		return true;
	}

}
