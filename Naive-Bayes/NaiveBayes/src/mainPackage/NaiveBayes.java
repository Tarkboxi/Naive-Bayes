package mainPackage;

import java.util.ArrayList;
import java.util.Scanner;

import dataArranger.DataHandler;
import dataStore.DataStore;
import probabilityFinder.ProbabilityFinder;


public class NaiveBayes {
	
	public static void main(String[] args) throws Exception 
    {
       	 DataStore dataStore = DataStore.get();
    	 String filePath = ""; 
    	 String fileType = "";
         System.out.println("Naive Bayes ALGORITHM : Enter the selection number for the dataset to use\n1 Mushroom\n2 Car\n3 Ecoli\n4 Letter-Recognition\n5 Breast cancer\n\nEnter a number from 1-5");
         Scanner inputScanner = new Scanner(System.in);
         String choice = inputScanner.nextLine();
         switch (choice)
         {
         case "1":
              filePath = "src\\dataFiles\\mushroom.data";
              fileType = "mushroom";
        	break;
         case "2":
        	  filePath = "src\\dataFiles\\car.data";
              fileType = "car";
        	 break;
         case "3":
        	 filePath = "src\\dataFiles\\ecoli.data";
             fileType = "ecoli";
        	 break;
         case "4":
        	 filePath = "src\\dataFiles\\letter-recognition.data";
             fileType = "letter";
        	 break;
         case "5":
        	 filePath = "src\\dataFiles\\breast-cancer-wisconsin.data";
             fileType = "bcancer";
        	 break;
         case "6":
        	 filePath = "src\\dataFiles\\testTennis.csv";
             fileType = "tennisTest";
        	 break;
         default:
        	 filePath = "";
        	 System.out.println("you entered wrong input, please run again");
        	 System.exit(0);
        	 break;		
         }
         
         inputScanner.close();       
         DataHandler inputHandler = new DataHandler();   
         ProbabilityFinder naiveBayesCalculator = new ProbabilityFinder();
         inputHandler.readAndStoreFile(filePath, fileType); 	
         System.out.println("created test data and training data, calculating please wait :");
         int crossValidationCounter = 1;
         for (int i = 9 ; i >= 0 ; i--)
         {
        	 inputHandler.CrossValidationDataGenerator(i); 
        	 inputHandler.likelihoodTableCreator();
        	 naiveBayesCalculator.findProbabilityAndTrainData();
        	 naiveBayesCalculator.predictTestDataResult();
        	 System.out.println("done "+crossValidationCounter+" time 5 cross Validation");
        	 crossValidationCounter = crossValidationCounter+1;
         }
         ArrayList<Float> listOfAccuracies =  dataStore.getListOfAccuracies();
         System.out.println("\n List of success rates recieved : "+listOfAccuracies);
         float sum = 0f;
         for (float singlePercentage : listOfAccuracies)
         {
        	 sum = sum+singlePercentage;
         }
         int count = listOfAccuracies.size();
         float mean = (float) (sum/count) ;       
         float sumOfDifferences = 0f;
         for (float singlePercentage : listOfAccuracies)
         {
        	float difference =  (singlePercentage - mean) ;
        	float differenceSquared = (difference * difference) ;
        	sumOfDifferences = sumOfDifferences+differenceSquared ;
         }
         float variance = sumOfDifferences / count ;
         float stdDeviation = (float) Math.sqrt(variance);
         System.out.println("std. Deviation ::"+stdDeviation);
         System.out.println("average percentage of accuracy ::"+mean);
    }

}
