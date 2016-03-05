package dataArranger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import dataStore.DataStore;

public class DataHandler {
	
	DataStore dataStore = DataStore.get();
	public void readAndStoreFile (String filePath, String fileType)
	{
		try 
		{
			File dataSetFile = new File(filePath);	
			FileInputStream fileReaderStream = new FileInputStream(dataSetFile);			 
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileReaderStream));
		 	ArrayList<ArrayList<String>> listOfAllLinesOfData  = new ArrayList<ArrayList<String>>();
	    	ArrayList<ArrayList<ArrayList<String>>> splitByFiveListOfData = new ArrayList<ArrayList<ArrayList<String>>>();
	    	ArrayList<ArrayList<String>> oneOfTheFiveData = new ArrayList<ArrayList<String>>();
	    
	    	String lineReadData = "";
	    	while((lineReadData=fileReader.readLine())!=null)
	    	{
	    		ArrayList<String> singleLineList = new ArrayList<String>();
	    		if (fileType.equalsIgnoreCase("ecoli"))
	    		{
		    		singleLineList = new ArrayList<String> (Arrays.asList(lineReadData.split("\\s+")) );
	    			singleLineList.remove(0);

	    		}
	    	
	    		else if (fileType.equalsIgnoreCase("bcancer"))
	    		{
		    		singleLineList = new ArrayList<String> (Arrays.asList(lineReadData.split(",")) );
	    			singleLineList.remove(0);

	    		}
	    		else if (fileType.equalsIgnoreCase("mushroom"))
	    		{
	    		    singleLineList = new ArrayList<String> (Arrays.asList(lineReadData.split(",")) );
//	    			if (singleLineList.contains("?"))
//	    			{
//	    				continue;  				
//	    			}

	    		}
	    		else
	    		{
	    		    singleLineList = new ArrayList<String> (Arrays.asList(lineReadData.split(",")) );
	    		}
	    		listOfAllLinesOfData.add(singleLineList);
	    	}
	    	
	    	fileReader.close();
	    	
	         if (fileType.equalsIgnoreCase("mushroom"))
	         {
	        	 dataStore.setTargetVariableColumnNumber(0);
	         }
	         if (fileType.equalsIgnoreCase("car"))
	         {
	        	 dataStore.setTargetVariableColumnNumber(6);
	         }
	         if (fileType.equalsIgnoreCase("ecoli"))
	         {
	        	 dataStore.setTargetVariableColumnNumber(7);
	        	 listOfAllLinesOfData =  new ArrayList<ArrayList<String>>( discretizer(listOfAllLinesOfData));
	         }
	         if (fileType.equalsIgnoreCase("bcancer"))
	         {
	        	 dataStore.setTargetVariableColumnNumber(9);
	         }
	         if (fileType.equalsIgnoreCase("letter"))
	         {
	        	 dataStore.setTargetVariableColumnNumber(0);
	         }
	         if (fileType.equalsIgnoreCase("tennisTest"))
	         {
	        	 dataStore.setTargetVariableColumnNumber(4);
	         }
	   	    		    	
	    		    	
	        Collections.shuffle(listOfAllLinesOfData); 
	    	int divisionByFive = listOfAllLinesOfData.size()/5;
	    	int numOfLines = listOfAllLinesOfData.size();	    
	    	int i =0;
	    	while(i < numOfLines)
	    	{
	    		oneOfTheFiveData.add(listOfAllLinesOfData.get(i));
	    		if (i!=0 && i % divisionByFive == 0)
	    		{    			
	    			if (! (splitByFiveListOfData.size() == 4 ))
	    			{
		    			splitByFiveListOfData.add(oneOfTheFiveData);
		    			oneOfTheFiveData = new ArrayList<ArrayList<String>>();
	    			}
	    		}
	    		if(i % divisionByFive == 0 && i == numOfLines-1)
	    		{
	    			splitByFiveListOfData.add(oneOfTheFiveData);
	    		}
	    		else if(i == numOfLines-1)
	    		{
	    			splitByFiveListOfData.add(oneOfTheFiveData);
	    		}
	    	i++;	
	    	}	    	
	    	dataStore.setTotalDataSplitIn5(splitByFiveListOfData);
		}  	
		catch (FileNotFoundException e) 
		{
			System.out.println("the file path specified does not contain the necessary file");
		}
		catch (IOException ioE) 
		{
			System.out.println("there is a file IO read exception");
		}
	}
	
	public void CrossValidationDataGenerator (int n)
	{
		int testDataColumnNumber = n;
    	DataStore dataStore = DataStore.get();
    	ArrayList<ArrayList<ArrayList<String>>> splitByFiveListOfData = new ArrayList<ArrayList<ArrayList<String>>>();
    	splitByFiveListOfData = dataStore.getTotalDataSplitIn5();
    	ArrayList<ArrayList<String>> listOfTestData  = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<String>> listOfTrainingData  = new ArrayList<ArrayList<String>>();
    	if (n > 4)
		{
    		if ( n == 7 || n == 8 )
    		{
    			testDataColumnNumber = testDataColumnNumber/2 -1 ;
    		}
    		else if ( n == 5 || n == 6)
    		{
    			testDataColumnNumber = (testDataColumnNumber/2) - 2 ; 
    		}
    		else if (n == 9)
    		{
    			testDataColumnNumber = testDataColumnNumber/2;
    		}
    		Collections.shuffle(splitByFiveListOfData);
		}
		else
		{	
			testDataColumnNumber = n;
		}
    	for (ArrayList<ArrayList<String>> oneOfTheFiveDataInner : splitByFiveListOfData)
    	{
    		if(oneOfTheFiveDataInner != splitByFiveListOfData.get(testDataColumnNumber) )
    		{
    			listOfTrainingData.addAll(oneOfTheFiveDataInner);
    		}
    			if (oneOfTheFiveDataInner == splitByFiveListOfData.get(testDataColumnNumber))
    			{
    				listOfTestData.addAll(oneOfTheFiveDataInner); 				
    			}
    	}
    	dataStore.setListOfTestData(listOfTestData);
    	dataStore.setListOfTrainingData(listOfTrainingData);
    	dataStore.setTrainingDataSize(listOfTrainingData.size());
	}
	
	public void likelihoodTableCreator()
	{
		int targetVariableColumnNumber = dataStore.getTargetVariableColumnNumber();
		ArrayList<ArrayList<String>> listOfTrainingData = new ArrayList<ArrayList<String>>();
		listOfTrainingData = dataStore.getListOfTrainingData();
		HashMap <Integer, ArrayList<String>> columnsOfAttributes  = new HashMap <Integer, ArrayList<String>>();
		HashMap <String, Integer> uniqueTargetCount = new HashMap <String, Integer>();
		HashMap <Integer, HashMap <String, HashMap<String, Integer>>> likelihoodTable = new HashMap <Integer, HashMap <String, HashMap<String, Integer>>>();
		for (int i=0; i < listOfTrainingData.get(0).size(); i++)
    	{
    		columnsOfAttributes.put(i, new ArrayList<String>());
    		likelihoodTable.put(i, new HashMap <String, HashMap<String, Integer>>());
    	}
		for(ArrayList<String> singleLineList : listOfTrainingData)
    	{
    		int index = 0;
    		for (String singleWord: singleLineList)	 
    		{
    			HashMap <String, HashMap<String, Integer>> columnLikelyhoodData = likelihoodTable.get(index);
    			
    			int updatedNumberforTarget = 0;
    			try
    			{
    				updatedNumberforTarget = columnLikelyhoodData.get(singleWord).get(singleLineList.get(targetVariableColumnNumber))+1;
    			}
    			catch(Exception e)
    			{
    				updatedNumberforTarget = 1;
    			}
				HashMap <String, Integer> targetCountForGivenValue = new HashMap <String, Integer> ();
    			try
    			{
    				targetCountForGivenValue = columnLikelyhoodData.get(singleWord);
    			}
    			catch (Exception e)
    			{
    				targetCountForGivenValue = new HashMap <String, Integer> ();
    			}
    			if ( null == targetCountForGivenValue)
    			{
    				targetCountForGivenValue = new HashMap <String, Integer> ();
    			}
    			targetCountForGivenValue.put(singleLineList.get(targetVariableColumnNumber), updatedNumberforTarget);
    			columnLikelyhoodData.put(singleWord, targetCountForGivenValue);
    			likelihoodTable.put(index, columnLikelyhoodData);
    			ArrayList<String> listOfvaluesUnderAttrib = new ArrayList<String>();   		
    			listOfvaluesUnderAttrib = columnsOfAttributes.get(index);
    			listOfvaluesUnderAttrib.add(singleWord);
    			columnsOfAttributes.put(index,listOfvaluesUnderAttrib);
    			if(index == dataStore.getTargetVariableColumnNumber())
    			{
    				try
    				{
    					uniqueTargetCount.put(singleWord, uniqueTargetCount.get(singleWord)+1);
    				}
    				catch (Exception e)
    				{
    					uniqueTargetCount.put(singleWord, 1);
    				}
    			}
    			index = index+1;
    		}    	
    	}
		dataStore.setUniqueTargetCount(uniqueTargetCount);
		dataStore.setLikelihoodTable(likelihoodTable);
	}
	
	public ArrayList<ArrayList<String>> discretizer(ArrayList<ArrayList<String>> listOfAllLinesOfData)
	{
		ArrayList<ArrayList<String>> discreteListOfAllLinesOfData = new ArrayList<ArrayList<String>>();
	
		for ( ArrayList<String> singleLine : listOfAllLinesOfData)
		{
			int index = 0;
			ArrayList<String> discreteNewLine = new ArrayList<String> (singleLine);
			for (String singleWord : singleLine)
			{
				if (index != 1 && index != 0 && index != 4 && index != 5 && index != 6)
				{
					index = index+1;
					continue;
				}
				float currentNumber = Float.parseFloat( singleWord );
				float newCurrentNumber = 0;
				if (index == 0)
				{
					ArrayList<Float> splitterList = new ArrayList<Float>();
					splitterList.add((float) 0.0);
					splitterList.add((float) 0.22);
					splitterList.add((float) 0.44);
					splitterList.add((float) 0.66);
					splitterList.add((float) 0.89);
					
					int indexOfSplitter = -1;
					for (float singleBound : splitterList)
					{
						indexOfSplitter = indexOfSplitter+1;
						if ( indexOfSplitter == 0)
						{
							continue;
						}
						else if (indexOfSplitter == 4)
						{
								newCurrentNumber = singleBound;
								break;
						}

						else if ( currentNumber < singleBound)
						{
							newCurrentNumber = splitterList.get(indexOfSplitter - 1);
							break;
						}						
					}
				}
				else if (index == 1)
				{
					ArrayList<Float> splitterList = new ArrayList<Float>();
					splitterList.add((float) 0.16);
					splitterList.add((float) 0.37);
					splitterList.add((float) 0.58);
					splitterList.add((float) 0.72);
					splitterList.add((float) 1.0);
					
					int indexOfSplitter = -1;
					for (float singleBound : splitterList)
					{
						indexOfSplitter = indexOfSplitter+1;
						if ( indexOfSplitter == 0)
						{
							continue;
						}
						else if (indexOfSplitter == 4)
						{
								newCurrentNumber = singleBound;
								break;
						}

						else if ( currentNumber < singleBound)
						{
							newCurrentNumber = splitterList.get(indexOfSplitter - 1);
							break;
						}						
					}
				}
				else if (index == 4)
				{
					ArrayList<Float> splitterList = new ArrayList<Float>();
					splitterList.add((float) 0.0);
					splitterList.add((float) 0.22);
					splitterList.add((float) 0.44);
					splitterList.add((float) 0.66);
					splitterList.add((float) 0.88);
					
					int indexOfSplitter = -1;
					for (float singleBound : splitterList)
					{
						indexOfSplitter = indexOfSplitter+1;
						if ( indexOfSplitter == 0)
						{
							continue;
						}
						else if (indexOfSplitter == 4)
						{
								newCurrentNumber = singleBound;
								break;
						}

						else if ( currentNumber < singleBound)
						{
							newCurrentNumber = splitterList.get(indexOfSplitter - 1);
							break;
						}						
					}
				}
				else if (index == 5)
				{
					ArrayList<Float> splitterList = new ArrayList<Float>();
					splitterList.add((float) 0.03);
					splitterList.add((float) 0.27);
					splitterList.add((float) 0.51);
					splitterList.add((float) 0.75);
					splitterList.add((float) 1.0);
					
					int indexOfSplitter = -1;
					for (float singleBound : splitterList)
					{
						indexOfSplitter = indexOfSplitter+1;
						if ( indexOfSplitter == 0)
						{
							continue;
						}
						else if (indexOfSplitter == 4)
						{
								newCurrentNumber = singleBound;
								break;
						}

						else if ( currentNumber < singleBound)
						{
							newCurrentNumber = splitterList.get(indexOfSplitter - 1);
							break;
						}						
					}
				}
				else if (index == 6)
				{
					ArrayList<Float> splitterList = new ArrayList<Float>();
					splitterList.add((float) 0.0);
					splitterList.add((float) 0.22);
					splitterList.add((float) 0.44);
					splitterList.add((float) 0.66);
					splitterList.add((float) 0.89);
					
					int indexOfSplitter = -1;
					for (float singleBound : splitterList)
					{
						indexOfSplitter = indexOfSplitter+1;
						if ( indexOfSplitter == 0)
						{
							continue;
						}
						else if (indexOfSplitter == 4)
						{
								newCurrentNumber = singleBound;
								break;
						}

						else if ( currentNumber < singleBound)
						{
							newCurrentNumber = splitterList.get(indexOfSplitter - 1);
							break;
						}						
					}
				}
				discreteNewLine.remove(index);
				discreteNewLine.add(index, String.valueOf( newCurrentNumber ) );
				index = index+1;				
			}
			discreteListOfAllLinesOfData.add(discreteNewLine);
		}
		return discreteListOfAllLinesOfData;
	}
}