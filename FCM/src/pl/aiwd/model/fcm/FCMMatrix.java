package pl.aiwd.model.fcm;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Random;

public class FCMMatrix {

	private int numberOfFactors; // liczba czynnikow mapy

	private double[][] fcmMatrix; // macierz fcm

	private ArrayList<String> factorsNames; // tablica nazw czynnikow
	private int actualIndex = 0; // index dla nastepnego elementu

	public FCMMatrix(int numb) {
		numberOfFactors = numb;

		fcmMatrix = new double[numberOfFactors][numberOfFactors];
		factorsNames = new ArrayList<String>();
	}

	public FCMMatrix(ArrayList<String> lista) {
		numberOfFactors = lista.size();

		fcmMatrix = new double[numberOfFactors][numberOfFactors];

		factorsNames = lista;
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
		
		try {
			fcm.setRelation("C1", "C3", 0.23);
		} catch (FactorNotPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(fcm.toString());
	}

}
