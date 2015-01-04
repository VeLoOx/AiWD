package pl.aiwd.parsers.map;

public class FCMParser {

	public static double[] matrixToVector(double[][] tab){
		
		int r = tab.length;
		int c = tab[0].length;
		
		int vecLenght = r*c;
		
		double[] vector = new double[vecLenght];
		int index = 0;
		for(int i=0;i<r;i++){
			for(int y=0;y<c;y++){
				vector[index] = tab[i][y];
				index++;
			}
		}
		
		
		return vector;
	}
	
	public static double[][] vectorToMatrix(double[] vec, int r, int c){
		double[][] matrix = new double[r][c];
		
		int ar=0; //aktualny wiersz
		int ac=0; //aktualna kolumna
		
		for(int i=0;i<vec.length;i++){
			matrix[ar][ac] = vec[i];
			ac++;
			if(ac==c){
				if(ar==r) break;
				ar++;
				ac=0;
			}
			
		}
		
		
		return matrix;
	}
	
	public static void printVector(double[] tab){
		System.out.println();
		for(int i=0;i<tab.length;i++){
			System.out.print(tab[i]+" - ");
		}
		System.out.println();
	}
	
}
