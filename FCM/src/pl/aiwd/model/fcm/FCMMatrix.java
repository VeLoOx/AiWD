package pl.aiwd.model.fcm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Random;

import pl.aiwd.parsers.map.FCMParser;

public class FCMMatrix implements Comparable<FCMMatrix> {

	private int numberOfFactors; // liczba czynnikow mapy

	private double[][] fcmMatrix; // macierz fcm

	private ArrayList<String> factorsNames; // tablica nazw czynnikow
	private int actualIndex = 0; // index dla nastepnego elementu
	
	private double error=0;
	private double fitnes=0;
	
	private boolean chosen=false; //czy juz wybrany z populacji podczas selekcji

	private  FCMMatrix() {
		// TODO Auto-generated constructor stub
		factorsNames = new ArrayList<String>();
		
	}
	
	public FCMMatrix(int numb) {
		this();
		numberOfFactors = numb;

		fcmMatrix = new double[numberOfFactors][numberOfFactors];
		
	}

	public FCMMatrix(ArrayList<String> lista) {
		this();
		numberOfFactors = lista.size();

		fcmMatrix = new double[numberOfFactors][numberOfFactors];

		factorsNames = lista;
	}
	
	public FCMMatrix(String[] lista) {
		this();
		numberOfFactors = lista.length;

		fcmMatrix = new double[numberOfFactors][numberOfFactors];

		for(int i=0;i<lista.length;i++)
			factorsNames.add(lista[i]);
	}

	public int getNumberOfFactors() {
		return numberOfFactors;
	}

	public void setNumberOfFactors(int numberOfFactors) {
		this.numberOfFactors = numberOfFactors;
	}

	public double[][] getFcmMatrix() {
		return fcmMatrix;
	}

	public void setFcmMatrix(double[][] fcmMatrix) {
		//this.fcmMatrix = fcmMatrix;
		for(int i=0;i<this.fcmMatrix.length;i++){
			for(int y=0;y<this.fcmMatrix[0].length;y++){
				this.fcmMatrix[i][y] = fcmMatrix[i][y];
			}
		}
	}

	public ArrayList<String> getFactorsNames() {
		return factorsNames;
	}

	public void setFactorsNames(ArrayList<String> factorsNames) {
		this.factorsNames = factorsNames;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	public double getFitnes() {
		return fitnes;
	}

	public void setFitnes(double fitnes) {
		this.fitnes = fitnes;
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	public void addFactor(String name) throws FullFCMException {
		if (actualIndex == numberOfFactors)
			throw new FullFCMException();
		factorsNames.add(name);
		actualIndex++;
	}

	public boolean factorPresent(String name) {
		return factorsNames.contains(name);
	}

	public void setRelation(String fFrom, String fTo, double relation)
			throws FactorNotPresentException {
		if (!factorPresent(fFrom))
			throw new FactorNotPresentException(fFrom);
		if (!factorPresent(fTo))
			throw new FactorNotPresentException(fTo);

		int indexFrom = factorsNames.indexOf(fFrom);
		int indexTo = factorsNames.indexOf(fTo);

		fcmMatrix[indexFrom][indexTo] = relation;
	}

	public void initializeFCMMatrix(double min, double max, double jump, int precision) {
		/*
		 * min - minimalna wartosc inicjalizacji max - maksymalna wartosc
		 * inicjalizacji jump - skok warrtosc inicjalizowanej
		 */
		Random r = new Random();
		
		int maxJumpNumber = (int) Math.round(Math.abs(max - min) / jump);
		//System.out.println("Max jump = "+maxJumpNumber);
		for (int i = 0; i < numberOfFactors; i++)
			for (int y = 0; y < numberOfFactors; y++) {
				if(i==y) {
					fcmMatrix[i][y]=0; //czynnik nie wplywa na samego siebie
					continue;
				}
				
				double val = min + (r.nextInt(maxJumpNumber) * jump);
				fcmMatrix[i][y] = (double)Math.round(val * Math.pow(10, precision)) / Math.pow(10, precision);
			}
	}
	
	public String toString(){
		
		String s = "    ";
		
		for (int i = 0; i < numberOfFactors; i++){
			s=s+factorsNames.get(i)+"     ";
		}
		s=s+"\n";
		
		for (int i = 0; i < numberOfFactors; i++){
						
			s=s+factorsNames.get(i)+": ";
			for (int y = 0; y < numberOfFactors; y++) {
					if(fcmMatrix[i][y]>=0)
					s = s+fcmMatrix[i][y]+"   "; else s = s+fcmMatrix[i][y]+"  ";
				
			}
			
			s=s+"\n";
		}
		s+="Blad = "+error+"\n Fitness = "+fitnes+"\n";
		return s;
	}

	
	
	public static void main(String[] args){
		FCMMatrix fcm = new FCMMatrix(4);
		
		try {
			fcm.addFactor("C1");
			fcm.addFactor("C2");
			fcm.addFactor("C3");
			fcm.addFactor("C4");
		} catch (FullFCMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fcm.initializeFCMMatrix(-0.2, 0.2, 0.05, 3);
		
		System.out.println(fcm.toString());
		
		FCMParser p = new FCMParser();
		
		double[] vec = p.matrixToVector(fcm.getFcmMatrix());
		p.printVector(vec);
		
		fcm.setFcmMatrix(p.vectorToMatrix(vec, fcm.getNumberOfFactors(), fcm.getNumberOfFactors()));
		
		/*try {
			fcm.setRelation("C1", "C3", 0.23);
		} catch (FactorNotPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		System.out.println(fcm.toString());
	}

	@Override
	public int compareTo(FCMMatrix o) {
		// TODO Auto-generated method stub
		return Double.compare(this.fitnes, o.getFitnes());
	}

}
