package dataStore;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStore {
	
	private int targetVariableColumnNumber;
	private static DataStore self = new DataStore();
	public static DataStore get() { return self; }
	private ArrayList<ArrayList<ArrayList<String>>> totalDataSplitIn5;
	private ArrayList<ArrayList<String>> listOfTestData;
	private ArrayList<ArrayList<String>> listOfTrainingData;
	private HashMap <Integer, HashMap <String, HashMap<String, Integer>>> likelihoodTable;
	private int trainingDataSize;
	private ArrayList<Float> listOfAccuracies;
	private HashMap <String, Integer> uniqueTargetCount;
	private HashMap <String, Float> priorProbabilityOfTargets;
	private HashMap<Integer, HashMap<String, HashMap<String, Float>>> listOfTrainedProbabilities;
	
	
	
	
	
	
	
	
	public ArrayList<Float> getListOfAccuracies() {
		return listOfAccuracies;
	}

	public void setListOfAccuracies(ArrayList<Float> listOfAccuracies) {
		this.listOfAccuracies = listOfAccuracies;
	}

	public HashMap<Integer, HashMap<String, HashMap<String, Float>>> getListOfTrainedProbabilities() {
		return listOfTrainedProbabilities;
	}

	public void setListOfTrainedProbabilities(
			HashMap<Integer, HashMap<String, HashMap<String, Float>>> listOfTrainedProbabilities) {
		this.listOfTrainedProbabilities = listOfTrainedProbabilities;
	}

	public HashMap<String, Float> getpriorProbabilityOfTargets() {
		return priorProbabilityOfTargets;
	}

	public void setpriorProbabilityOfTargets(HashMap<String, Float> priorProbabilityOfTargets) {
		this.priorProbabilityOfTargets = priorProbabilityOfTargets;
	}

	public HashMap<String, Integer> getUniqueTargetCount() {
		return uniqueTargetCount;
	}

	public void setUniqueTargetCount(HashMap<String, Integer> uniqueTargetCount) {
		this.uniqueTargetCount = uniqueTargetCount;
	}

	public int getTrainingDataSize() {
		return trainingDataSize;
	}

	public void setTrainingDataSize(int trainingDataSize) {
		this.trainingDataSize = trainingDataSize;
	}

	public HashMap<Integer, HashMap<String, HashMap<String, Integer>>> getLikelihoodTable() {
		return likelihoodTable;
	}

	public void setLikelihoodTable(HashMap<Integer, HashMap<String, HashMap<String, Integer>>> likelihoodTable) {
		this.likelihoodTable = likelihoodTable;
	}
	public ArrayList<ArrayList<String>> getListOfTestData() {
		return listOfTestData;
	}

	public void setListOfTestData(ArrayList<ArrayList<String>> listOfTestData) {
		this.listOfTestData = listOfTestData;
	}

	public ArrayList<ArrayList<String>> getListOfTrainingData() {
		return listOfTrainingData;
	}

	public void setListOfTrainingData(ArrayList<ArrayList<String>> listOfTrainingData) {
		this.listOfTrainingData = listOfTrainingData;
	}

	public ArrayList<ArrayList<ArrayList<String>>> getTotalDataSplitIn5() {
		return totalDataSplitIn5;
	}

	public void setTotalDataSplitIn5(ArrayList<ArrayList<ArrayList<String>>> totalDataSplitIn5) {
		this.totalDataSplitIn5 = totalDataSplitIn5;
	}

	public int getTargetVariableColumnNumber() {
		return targetVariableColumnNumber;
	}

	public void setTargetVariableColumnNumber(int targetVariableColumnNumber) {
		this.targetVariableColumnNumber = targetVariableColumnNumber;
	}


}
