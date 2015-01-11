package pl.aiwd.calculating;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.aiwd.calculating.transformation.LogisticTransform;
import pl.aiwd.calculating.transformation.Tranformator;
import pl.aiwd.model.fcm.FCMMatrix;
import pl.aiwd.parsers.datafiles.CSVParser;
import pl.aiwd.parsers.map.FCMParser;

public class FCMArea {
	
	private String pathPattern; //sciezka do wzorca
	private String namePattern; //nazwa wzorca
	private String pathNormalizePattern; //sciezka dla znormalizowanego wzorca
	private String nameNormalizePattern; //nazwa znormalizowanego wzorca
	
	private List<double[]> patternRecords; /*rekrdy wzorcowe*/
	private List<double[]> normalizedPatternRecords; /*rekordy wzorcowe znormalizowane*/
	private List<double[]> normalizedTestPatternRecords;
	private List<double[]> computatedRecords; /*rekordy wyliczone (wzorcowe-1)*/
	
	private FCMParser fcmParser;
	private CSVParser csvNormalizedParser; //parser od znormalizowanych rekordow
	private CSVParser csvParser; //parser od znormalizowanych rekordow
	private Tranformator transformator;
	private double testPart = 0.1;
	
	private boolean show=true;
	
	public FCMArea(){
		fcmParser = new FCMParser();
		csvNormalizedParser = new CSVParser();
		csvParser = new CSVParser();
		
		transformator = new Tranformator(new LogisticTransform());
		normalizedTestPatternRecords = new ArrayList<>();
	}
	
	public FCMArea(CSVParser normalized){
		this();
		
		csvNormalizedParser = normalized;
		
	}
	
	public FCMArea(String pp, String np, String pnp, String nnp){
		this();
		
		pathPattern = pp;
		namePattern = np;
		pathNormalizePattern = pnp;
		nameNormalizePattern = nnp;
		
	}
	
	public String getPathPattern() {
		return pathPattern;
	}

	public void setPathPattern(String pathPattern) {
		this.pathPattern = pathPattern;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	public String getPathNormalizePattern() {
		return pathNormalizePattern;
	}

	public void setPathNormalizePattern(String pathNormalizePattern) {
		this.pathNormalizePattern = pathNormalizePattern;
	}

	public String getNameNormalizePattern() {
		return nameNormalizePattern;
	}

	public void setNameNormalizePattern(String nameNormalizePattern) {
		this.nameNormalizePattern = nameNormalizePattern;
	}

	public List<double[]> getPatternRecords() {
		return patternRecords;
	}

	public void setPatternRecords(List<double[]> patternRecords) {
		this.patternRecords = patternRecords;
	}

	public List<double[]> getNormalizedPatternRecords() {
		return normalizedPatternRecords;
	}

	public void setNormalizedPatternRecords(List<double[]> normalizedPatternRecords) {
		this.normalizedPatternRecords = normalizedPatternRecords;
		int numb =(int) (testPart*this.normalizedPatternRecords.size());
		for(int pos=0;pos<numb;pos++){
			normalizedTestPatternRecords.add(this.normalizedPatternRecords.remove(pos));
		}
	}

	public FCMParser getFcmParser() {
		return fcmParser;
	}

	public void setFcmParser(FCMParser fcmParser) {
		this.fcmParser = fcmParser;
	}

	public CSVParser getCsvNormalizedParser() {
		return csvNormalizedParser;
	}

	public void setCsvNormalizedParser(CSVParser csvNormalizedParser) {
		this.csvNormalizedParser = csvNormalizedParser;
	}

	public CSVParser getCsvParser() {
		return csvParser;
	}

	public void setCsvParser(CSVParser csvParser) {
		this.csvParser = csvParser;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public void loadPatternRecords(){
		
		patternRecords = csvParser.loadRecordsToList(pathPattern, namePattern);	
	}
	
	public void loadPatternRecords(String pp, String np){
		
		patternRecords = csvParser.loadRecordsToList(pp, np);
		
	}
	
	public void loadNormalizedPatternRecords(){
		normalizedPatternRecords = csvNormalizedParser.loadRecordsToList(pathNormalizePattern, nameNormalizePattern);
		int numb =(int) (testPart*normalizedPatternRecords.size());
		for(int pos=0;pos<numb;pos++){
			normalizedTestPatternRecords.add(normalizedPatternRecords.remove(pos));
		}
	}
	
	public void loadNormalizedPatternRecords(String pnp, String nnp){
		normalizedPatternRecords = csvNormalizedParser.loadRecordsToList(pnp, nnp);
	}
	
	public List<double[]> getNormalizedTestPatternRecords() {
		return normalizedTestPatternRecords;
	}

	public void setNormalizedTestPatternRecords(
			List<double[]> normalizedTestPatternRecords) {
		this.normalizedTestPatternRecords = normalizedTestPatternRecords;
	}

	public double getTestPart() {
		return testPart;
	}

	public void setTestPart(double testPart) {
		this.testPart = testPart;
	}

	public void showNormalizedPatternRecordList(){
		for (double[] d : normalizedPatternRecords){
			String line = "";
			for(int i=0;i<d.length;i++){
				line+=d[i]+";";
			}
			System.out.println(line);
		}
	}
	
	//---------------
	
	public List<FCMMatrix> initializeFCMMatrixPool(int number, CSVParser cvs){
		List<FCMMatrix> list = new ArrayList<>();
		
		for(int i=0;i<number;i++){
			FCMMatrix fcm = new FCMMatrix(cvs.getColumnNames());
			fcm.initializeFCMMatrix(-0.9, 0.9, 0.01, 3);
			list.add(fcm);
		}
		
		return list;
	}
	
	
	
	/*wylicza nowe czynniki na podsatwie macierzy*/
	public List<double[]> computateNewFactors(List<double[]> pattern, FCMMatrix matrix){
		double[][] mx = matrix.getFcmMatrix(); //pobranie macierzy
		List<double[]> newRecList = new ArrayList<double[]>();
		
		Iterator<double[]> it = pattern.iterator();
		while(it.hasNext()){
			double[] rec = it.next();
			double[] newRec = new double[rec.length];
			//System.out.println("-------");
			for(int i=0;i<rec.length;i++){
				double sum=0;
				String line="";
				
				for(int y=0;y<mx[i].length;y++){
					
					line +=mx[i][y]+"*"+rec[y]+" + ";
					sum = sum+mx[i][y]*rec[y];
				}
				if(show)System.out.println(line);
				newRec[i] = transformator.transform(sum);
			}
			
			newRecList.add(newRec);
		}
		return newRecList;
	}
	
	public List<double[]> computateNewFactors(FCMMatrix matrix){
		return computateNewFactors(normalizedPatternRecords, matrix);
		
	}
	
	public double testMap(FCMMatrix m){
		
		List<double[]> newFact = computateNewFactors(normalizedTestPatternRecords, m);//nowe rekordy na podst tesu i mapy
		
		int recnumb = newFact.size();
		int cnumb = newFact.get(0).length;
		double error = 0;
		
		double sum = 0;
		
		for (int r=0;r<recnumb-1;r++){
			sum = 0;
		double[] rec = normalizedPatternRecords.get(r+1);
		double[] newr = newFact.get(r);
		for(int i=0;i<cnumb;i++){
			double val = Math.pow((rec[i]-newr[i]),2)/cnumb;
			sum = sum+val;
		}
		
		error = error+sum;
		}
		
		double val2 =  error/recnumb;
		error = val2 * 100;
		return error;
	}
	
	public double computateJError(List<double[]> pattern, List<double[]> candidate){
		
		double error = 0;
		int rec = 0; //liczba przetworzonych rekordow
		int cnumb = pattern.get(0).length;
		int recnumb = pattern.size();
		for(int k=0;k<recnumb-1;k++){
			rec++;
			double[] cpat = pattern.get(k+1);
			double[] ccat = candidate.get(k);
			
			for(int i=0;i<cnumb;i++){
				error+= Math.pow(Math.abs(cpat[i]-ccat[i]),2) / ((recnumb-1)*cnumb);
			}
		}
		if(show)System.out.println("\nLiczba par dla bledu = "+rec);
		return error;
	}
	
	public double computateJError(List<double[]> candidate){
		return computateJError(normalizedPatternRecords,candidate);
	}
	
	public double computateFitness(double val){
		return 1/(val);
	}
	

}
