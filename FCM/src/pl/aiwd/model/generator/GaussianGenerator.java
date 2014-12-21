package pl.aiwd.model.generator;
import java.util.Random;


public class GaussianGenerator {
	Random generator = new Random();
	private static GaussianGenerator instance=null;
	private GaussianGenerator(){		
	}

	public static GaussianGenerator getGenerator(){
		if(instance==null) instance=new GaussianGenerator();
		return instance;
	}
	
	public double getNormalDistNumber(double min, double max){
		double mean = (min+max)/2;
		double std = (max-mean)/2;
		return generator.nextGaussian()*(std)+(mean);
		
	}
}