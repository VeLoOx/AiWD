package pl.aiwd.test;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import pl.aiwd.calculating.FCMArea;
import pl.aiwd.calculating.GENArea;
import pl.aiwd.model.fcm.FCMMatrix;
import pl.aiwd.parsers.datafiles.CSVParser;
import pl.aiwd.parsers.map.FCMParser;

public class TestFcm {
	
	public static void main(String[] args){
		String csvFilePath = "D:\\Studia\\przedmioty2_2\\AiWD\\Projekt2\\git\\FCM\\Data\\";
		String csvFileName = "data.csv";
		String newCsvFileName = "normalizedata.csv";
				
		CSVParser csv = new CSVParser();
		csv.setComunicates(true);
		
		/*zaladowanie zwyklego pliku i wyznaczenie maxi i min kazdej kolumny*/
		FileInputStream readFile = csv.loadFile(csvFilePath, csvFileName);
		csv.procesMinMaxFile(readFile);
		csv.showMap(); //mapa min/max czynnikow
		
		//przeprowadzenie normalnizacji
		csv.procesNormalizeFile(csvFilePath, newCsvFileName, readFile, 0, 1);
		
		CSVParser csvNormal = new CSVParser();
		FileInputStream readFileNorm = csvNormal.loadFile(csvFilePath, newCsvFileName);
		/*przekopiowalem sobie liczbe kolumn zeby nie musiec znowy robic procesMinMaxFile()*/
		csvNormal.setColumnNumber(csv.getColumnNumber()); 
		csvNormal.setColumnNames(csv.getColumnNames());
		/*ustawienie znormalizowanej listy
		 * mozna tez zrobic z poziomu FCMArea ale wole tak jakos jest 
		 * bardziej przejzyscie, ale delegatow nie usuwalem*/
		
		FCMArea fcmarea = new FCMArea(csvNormal);
		fcmarea.setNormalizedPatternRecords(csvNormal.loadRecordsToList(readFileNorm)); 
		
		System.out.println("Elementy na liscie");
		fcmarea.showNormalizedPatternRecordList();
		
		fcmarea.setShow(false);
		
		GENArea gen = new GENArea(fcmarea);
		gen.makePool(100);
		gen.runFitnesCalculation();
		
		Collections.sort(gen.getPool());
		int i=0;
		for(FCMMatrix m : gen.getPool()){
			System.out.println("Fitness "+i+" = "+m.getFitnes()+"  err="+m.getError());
			i++;
		}
		
		System.out.println("Sum Fitness = "+gen.getPoolFitness());
		FCMMatrix m2 = gen.getPool().get(gen.getPool().size()-1);
		System.out.println("Fitness "+i+" = "+m2.getFitnes()+"  err="+m2.getError());
		double err2 = fcmarea.testMap(m2);
		System.out.println("Error = % "+err2);
		
		//gen.makeNewPopulation();
		
		gen.runGA(300);
		
		gen.runFitnesCalculation();
		Collections.sort(gen.getPool());
	    i=0;
		for(FCMMatrix m : gen.getPool()){
			System.out.println("Fitness "+i+" = "+m.getFitnes()+"  err="+m.getError());
			i++;
		}
		System.out.println("Sum Fitness = "+gen.getPoolFitness());
		
		FCMMatrix m = gen.getPool().get(gen.getPool().size()-1);
		System.out.println("Fitness "+i+" = "+m.getFitnes()+"  err="+m.getError());
		double err = fcmarea.testMap(m);
		System.out.println("Error = % "+err);
		//System.out.println("SelFitness = "+gen.selection(gen.getPool()).getFitnes());
		
		/*FCMMatrix m1 = gen.selection(gen.getPool());
		FCMMatrix m2 = gen.selection(gen.getPool());
		
		FCMParser.printVector(FCMParser.matrixToVector(m1.getFcmMatrix()));
		FCMParser.printVector(FCMParser.matrixToVector(m2.getFcmMatrix()));
		
		FCMMatrix l = gen.crossover(m1, m2);
		
		
			System.out.println("dziecko: "+l.getNumberOfFactors());
			System.out.println(l.toString());
			FCMParser.printVector(FCMParser.matrixToVector(l.getFcmMatrix()));
			
		boolean bylo = false;
		while(!bylo){
			bylo = gen.mutation(l);
		}
		System.out.println(l.toString());*/
		
			
		//wybranie pierwszej (na probe) z macierzy z wygenerowanje puli
		/*FCMMatrix matrix = fcmarea.initializeFCMMatrixPool(1, csv).get(0);
		
		System.out.println("NOWE CZYNNIKI !!!");
		System.out.println(matrix.toString());
		System.out.println();
		
		List<double[]> newFactorsVal = fcmarea.computateNewFactors(matrix);
		
		for(double[] d : newFactorsVal){
			String line="";
			for(int i=0;i<d.length;i++){
				line+=d[i]+";";
			}
			System.out.println(line);
		}
		
		System.out.println(newFactorsVal.size());
		
		double err = fcmarea.computateJError(newFactorsVal);
		System.out.print("Blad = "+err);
		System.out.print("Fitness = "+fcmarea.computateFitness(err));*/
	}

}
