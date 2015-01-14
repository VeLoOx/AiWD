package pl.aiwd.controler;

import java.io.FileInputStream;
import java.util.Collections;

import pl.aiwd.calculating.FCMArea;
import pl.aiwd.calculating.GENArea;
import pl.aiwd.model.fcm.FCMMatrix;
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
	
	public double fitbef;
	public double fitaft;
	public double errbef;
	public double erraft;
	public double time;
	public int iteration;
	public FCMMatrix mbef;
	public FCMMatrix maft;
	
	private String message = "";
	
	private Controler(){
		csv = new CSVParser();
		csv.setComunicates(false);
		
		csvNormal = new CSVParser();
		csvNormal.setComunicates(false);
	}
	
	public void runGA(int iter, int pool){
		gen.makePool(pool);
		gen.runFitnesCalculation();
		Collections.sort(gen.getPool());
		mbef = gen.getPool().get(gen.getPool().size()-1);
		//System.out.println(+m2.getFitnes()+"  err="+m2.getError());
		fitbef = mbef.getFitnes();
		double err2 = fcmarea.testMap(mbef);
		errbef = err2;
		//System.out.println("Error = % "+err2);
		double t1 = System.currentTimeMillis();
		iteration = gen.runGA(iter);
		double t2 = System.currentTimeMillis();
		time = t2-t1;
		
		gen.runFitnesCalculation();
		Collections.sort(gen.getPool());
		maft = gen.getPool().get(gen.getPool().size()-1);
		fitaft =  maft.getFitnes();
		double err3 = fcmarea.testMap( maft);
		erraft = err3;
		
	}
	
	public void runGA(double fit, int pool){
		gen.makePool(pool);
		gen.runFitnesCalculation();
		Collections.sort(gen.getPool());
		mbef = gen.getPool().get(gen.getPool().size()-1);
		//System.out.println(+m2.getFitnes()+"  err="+m2.getError());
		fitbef = mbef.getFitnes();
		double err2 = fcmarea.testMap(mbef);
		errbef = err2;
		//System.out.println("Error = % "+err2);
		double t1 = System.currentTimeMillis();
		iteration = gen.runGA(fit);
		double t2 = System.currentTimeMillis();
		time = t2-t1;
		
		gen.runFitnesCalculation();
		Collections.sort(gen.getPool());
		maft = gen.getPool().get(gen.getPool().size()-1);
		fitaft =  maft.getFitnes();
		double err3 = fcmarea.testMap( maft);
		erraft = err3;
		
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
		fcmarea.setShow(false);
		fcmarea.setNormalizedPatternRecords(csvNormal.loadRecordsToList(readFileNorm)); 
		fcmarea.setPatternRecords(csv.loadRecordsToList(readFile));
		fcmarea.showNormalizedPatternRecordList();
		gen = new GENArea(fcmarea);
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
