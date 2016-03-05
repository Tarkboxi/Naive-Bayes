package probabilityFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


import dataStore.DataStore;

public class ProbabilityFinder {
	DataStore dataStore = DataStore.get();
	int trainingDataSize = 0;
	
	public void findProbabilityAndTrainData ()
	{
		HashMap <String, Integer> uniqueTargetCount = dataStore.getUniqueTargetCount();
		trainingDataSize = dataStore.getTrainingDataSize();
		HashMap <Integer, HashMap <String, HashMap<String, Integer>>> likelihoodTable = dataStore.getLikelihoodTable();
		HashMap <String, Float> priorProbOfTargets = new HashMap<String,Float>();
		int targetColumnNumber = dataStore.getTargetVariableColumnNumber();
		for (String singleTargetValue : uniqueTargetCount.keySet())
		{
			float probOfThisTarget = (float) uniqueTargetCount.get(singleTargetValue) / (float) trainingDataSize ;
			priorProbOfTargets.put(singleTargetValue, probOfThisTarget);
		}
		dataStore.setpriorProbabilityOfTargets(priorProbOfTargets);
		HashMap<Integer, HashMap<String, HashMap<String, Float>>> listOfTrainedProbabilities = new HashMap<Integer, HashMap<String, HashMap<String, Float>>>();
		for (int columnNumber : likelihoodTable.keySet())
		{
			if (columnNumber == targetColumnNumber)
			{
				continue;
			}
			HashMap<String, HashMap<String, Float>> singleColumnTrainedProbs = new HashMap<String, HashMap<String, Float>>();
			singleColumnTrainedProbs = listOfTrainedProbabilities.get(columnNumber);
			if (null == singleColumnTrainedProbs)
			{
				singleColumnTrainedProbs = new HashMap<String, HashMap<String, Float>>();
			}
			for (String singleUniqueValue : likelihoodTable.get(columnNumber).keySet())
			{
				HashMap<String, Float> probablityHolder = new HashMap <String, Float>();
				HashMap<String, Integer> likelyHoodDataOfValue = likelihoodTable.get(columnNumber).get(singleUniqueValue);
				for (String singleTarget : likelyHoodDataOfValue.keySet())
				{
					int numberOfCurrentTargetRepetition = likelyHoodDataOfValue.get(singleTarget);
					float probabilityOfcurrentTargetForValue = (float) numberOfCurrentTargetRepetition / (float)uniqueTargetCount.get(singleTarget) ;
					probablityHolder.put(singleTarget, probabilityOfcurrentTargetForValue);
				}
				singleColumnTrainedProbs.put(singleUniqueValue,probablityHolder);
			}
			listOfTrainedProbabilities.put(columnNumber, singleColumnTrainedProbs);
		}
		dataStore.setListOfTrainedProbabilities(listOfTrainedProbabilities);
	}
	
	public void predictTestDataResult()
	{
		HashMap<Integer, HashMap<String, HashMap<String, Float>>> listOfTrainedProbabilities = dataStore.getListOfTrainedProbabilities();
	//	System.out.println(listOfTrainedProbabilities);
		ArrayList<ArrayList<String>> listOfTestData = dataStore.getListOfTestData();
		HashMap <String, Float> priorProbOfTargets = dataStore.getpriorProbabilityOfTargets();
	//	System.out.println(priorProbOfTargets);
		int targetColumnNumber = dataStore.getTargetVariableColumnNumber();
		int successPredicts = 0;
		for (ArrayList<String> singleLineOfTest : listOfTestData)
			{
				HashMap <String, ArrayList<Float>> likelihoodMapper = new HashMap <String, ArrayList<Float>>();
				int index = 0;			
			//	System.out.println("this is the line :"+singleLineOfTest);
				for (String singleWord : singleLineOfTest)
				{
					if (index == targetColumnNumber)
					{
						index= index+1;
						continue;
					}
					HashMap<String, Float> listOfProbsForValue = listOfTrainedProbabilities.get(index).get(singleWord);
			//		System.out.println("wut  "+singleWord+"   "+listOfProbsForValue);
					for (String singleTarget : priorProbOfTargets.keySet())
					{
						ArrayList<Float> likeLihoodProducts = new ArrayList<Float>();
						likeLihoodProducts = likelihoodMapper.get(singleTarget);
						if ( null == likeLihoodProducts)
						{
							likeLihoodProducts = new ArrayList<Float>();
						}
						try
						{
							float probOfValueForTarget = listOfProbsForValue.get(singleTarget);
							likeLihoodProducts.add(probOfValueForTarget);
							likelihoodMapper.put(singleTarget, likeLihoodProducts);
						}
						catch(Exception e)
						{
							float probOfValueForTarget = 0;
							likeLihoodProducts.add(probOfValueForTarget);
							likelihoodMapper.put(singleTarget, likeLihoodProducts);
							continue;
						}
					}
			//		System.out.println("grimwaa  "+likelihoodMapper);
					index = index+1;
				}
				String maxNormalizedTarget = "";
				float maxNormalized = 0;
				for (String singleTarget : likelihoodMapper.keySet())
				{
					ArrayList<Float> singleTargetlikelihoodList = likelihoodMapper.get(singleTarget);
			//		System.out.println(singleTarget+"  "+singleTargetlikelihoodList);
					float totalProb = 1;
					for (float singleProb : singleTargetlikelihoodList)
					{
						totalProb = totalProb*singleProb;
					}
			//		System.out.println("coming for :"+singleTarget+ " : "+totalProb);
					if (maxNormalized < totalProb)
					{
						maxNormalized = totalProb;
						maxNormalizedTarget = singleTarget;
					}
				}
			//	System.out.println("the big guy was : "+maxNormalizedTarget);
				if (maxNormalizedTarget.equalsIgnoreCase(singleLineOfTest.get(targetColumnNumber)))
				{
					successPredicts = successPredicts+1;
			//		System.out.println("one success");
				}
			}
			float accuracy = (float) successPredicts / (float) listOfTestData.size();
			accuracy = (float) accuracy * (float) 100 ;
			ArrayList<Float> accuracyList = new ArrayList<Float>();
			accuracyList = dataStore.getListOfAccuracies();
			if (null == accuracyList)
			{
				accuracyList =  new ArrayList<Float>();
			}
			accuracyList.add(accuracy);
			dataStore.setListOfAccuracies(accuracyList);
	}
	
}
