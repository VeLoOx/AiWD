package pl.aiwd.calculating.transformation;

public class LogisticTransform implements ITransform {
	
	private double e = Math.E; //podstawa algorytmu naturalnego
	private double C = 5; //determinuje wlasciwy ksztalt funkcji

	@Override
	public double transform(double val) {
		// TODO Auto-generated method stub
		if (val==0) return val;
		
		double powVal = (-C)*val;
		//powVal = -powVal;
		
		return 1/(1+Math.pow(e, powVal));
	}
	
	public static void main(String[] args){
		LogisticTransform lg = new LogisticTransform();
		
		System.out.println(lg.transform(0.053333333333333344));
	}

	

}
