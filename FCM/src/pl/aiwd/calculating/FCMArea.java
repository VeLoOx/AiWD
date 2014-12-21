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
	private List<double[]> computatedRecords; /*rekordy wyliczone (wzorcowe-1)*/
	
	private FCMParser fcmParser;
	private CSVParser csvNormalizedParser; //parser od znormalizowanych rekordow
	private CSVParser csvParser; //parser od znormalizowanych rekordow
	private Tranformator transformator;
	
	private boolean show=true;
	
	public FCMArea(){
		fcmParser = new FCMParser();
		csvNormalizedParser = new CSVParser();
		csvParser = new CSVParser();
		
		transformator = new Tranformator(new LogisticTransform());
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
	}
	
	public void loadNormalizedPatternRecords(String pnp, String nnp){
		normalizedPatternRecords = csvNormalizedParser.loadRecordsToList(pnp, nnp);
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
			fcm.initializeFCMMatrix(-0.2, 0.2, 0.05, 3);
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
			System.out.println("-------");
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
	

}
