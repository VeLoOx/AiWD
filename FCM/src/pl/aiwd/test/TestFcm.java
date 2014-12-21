package pl.aiwd.test;

import java.io.FileInputStream;
import java.util.List;

import pl.aiwd.calculating.FCMArea;
import pl.aiwd.model.fcm.FCMMatrix;
import pl.aiwd.parsers.datafiles.CSVParser;

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
		
		/*ustawienie znormalizowanej listy
		 * mozna tez zrobic z poziomu FCMArea ale wole tak jakos jest 
		 * bardziej przejzyscie, ale delegatow nie usuwalem*/
		FCMArea fcmarea = new FCMArea(csvNormal);
		fcmarea.setNormalizedPatternRecords(csvNormal.loadRecordsToList(readFileNorm)); 
		
		System.out.println("Elementy na liscie");
		fcmarea.showNormalizedPatternRecordList();
		
			
		//wybranie pierwszej (na probe) z macierzy z wygenerowanje puli
		FCMMatrix matrix = fcmarea.initializeFCMMatrixPool(1, csv).get(0);
		
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
	}

}
