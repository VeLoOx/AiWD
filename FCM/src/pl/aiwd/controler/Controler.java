package pl.aiwd.controler;

import java.io.FileInputStream;

import pl.aiwd.calculating.FCMArea;
import pl.aiwd.calculating.GENArea;
import pl.aiwd.parsers.datafiles.CSVParser;
import sun.security.jca.GetInstance;

public class Controler {
	private static Controler con = null;
	String csvFilePath = "D:\\Studia\\przedmioty2_2\\AiWD\\Projekt2\\git\\FCM\\Data\\";
	String csvFileName = "data.csv";
	String newCsvFileName = "normalizedata.csv";
	
	CSVParser csv;
	FileInputStream readFile;
	boolean loadedData = false;
	
	CSVParser csvNormal;
	FileInputStream readFileNorm;
	
	FCMArea fcmarea = null;
	GENArea gen = null;
	
	private String message = "";
	
	private Controler(){
		csv = new CSVParser();
		csv.setComunicates(false);
		
		csvNormal = new CSVParser();
		csvNormal.setComunicates(false);
	}
	
	public String loadData(String path, String name){
		readFile = csv.loadFile(path, name);
		
		if(readFile!=null){
			csvFilePath = path;
			loadedData=true; 
			return "File loaded";
			} else {loadedData=false; return "File error";}
	}
	
	public boolean normalizeData(){
		if(!loadedData) {message = "You must load data first"; return false;}
		
		csv.procesMinMaxFile(readFile);
		csv.showMap(); //mapa min/max czynnikow
		
		//przeprowadzenie normalnizacji
		csv.procesNormalizeFile(csvFilePath, newCsvFileName, readFile, 0, 1);
		message = "Data normalized "+csvFilePath+newCsvFileName;
		
		readFileNorm = csvNormal.loadFile(csvFilePath, newCsvFileName);
		csvNormal.setColumnNumber(csv.getColumnNumber()); 
		csvNormal.setColumnNames(csv.getColumnNames());
		fcmarea = new FCMArea(csvNormal);
		fcmarea.setNormalizedPatternRecords(csvNormal.loadRecordsToList(readFileNorm)); 
		fcmarea.setPatternRecords(csv.loadRecordsToList(readFile));
		fcmarea.showNormalizedPatternRecordList();
		return true;
	}
	
	public static Controler getInstance(){
		if(con==null) con = new Controler();
		return con;
	}

	public String getMessage() {
		return message;
	}

	public FCMArea getFcmarea() {
		return fcmarea;
	}

	public void setFcmarea(FCMArea fcmarea) {
		this.fcmarea = fcmarea;
	}

	public GENArea getGen() {
		return gen;
	}

	public void setGen(GENArea gen) {
		this.gen = gen;
	}

	public CSVParser getCsv() {
		return csv;
	}

	public void setCsv(CSVParser csv) {
		this.csv = csv;
	}

	public CSVParser getCsvNormal() {
		return csvNormal;
	}

	public void setCsvNormal(CSVParser csvNormal) {
		this.csvNormal = csvNormal;
	}

}
