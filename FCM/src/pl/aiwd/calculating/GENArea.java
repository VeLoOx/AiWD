package pl.aiwd.calculating;

import java.util.List;

import pl.aiwd.model.fcm.FCMMatrix;

public class GENArea {
	
	private int poolSize = 10;
	private int maxIteration = 100;
	
	
	
	private List<FCMMatrix> pool;
	private FCMArea fcmarea;
	
	public GENArea (FCMArea area){
		/*area musi juz bycpoustawiana, miec zaladowane parsery, listy wzorcow*/
		fcmarea = area;
	}
	
	public void makePool(int size){
		poolSize = size;
		pool = fcmarea.initializeFCMMatrixPool(size, fcmarea.getCsvNormalizedParser());
	}
	public void makePool(){
		
		pool = fcmarea.initializeFCMMatrixPool(poolSize, fcmarea.getCsvNormalizedParser());
	}
	
	
	public void runFitnesCalculation(){
		for(FCMMatrix m : pool){
			List<double[]> newFactorsVal = fcmarea.computateNewFactors(m); //nowe wart czynnikow
			m.setError(fcmarea.computateJError(newFactorsVal)); //wyliczenie bledu
			m.setFitnes(fcmarea.computateFitness(m.getError())); //wyliczenie fitness
		}
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getMaxIteration() {
		return maxIteration;
	}

	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
	}

	public List<FCMMatrix> getPool() {
		return pool;
	}

	public void setPool(List<FCMMatrix> pool) {
		this.pool = pool;
	}

	public FCMArea getFcmarea() {
		return fcmarea;
	}

	public void setFcmarea(FCMArea fcmarea) {
		this.fcmarea = fcmarea;
	}
	

}
