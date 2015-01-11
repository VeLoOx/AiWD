package pl.aiwd.calculating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pl.aiwd.model.fcm.FCMMatrix;
import pl.aiwd.model.generator.GaussianGenerator;
import pl.aiwd.parsers.map.FCMParser;

public class GENArea {

	private int poolSize = 10;
	private int maxIteration = 100;

	private double sumFit = 0;

	private List<FCMMatrix> pool;
	private List<FCMMatrix> tmpPool;
	private List<FCMMatrix> newPool;
	private FCMArea fcmarea;
	
	private double crossoverFraction = 0.9;
	

	private GaussianGenerator GG;

	public GENArea(FCMArea area) {
		/* area musi juz bycpoustawiana, miec zaladowane parsery, listy wzorcow */
		fcmarea = area;
		newPool = new ArrayList<FCMMatrix>();
		tmpPool = new ArrayList<FCMMatrix>();
		GG = GaussianGenerator.getGenerator();
	}

	public void makePool(int size) {
		poolSize = size;
		pool = fcmarea.initializeFCMMatrixPool(size,
				fcmarea.getCsvNormalizedParser());
	}

	public void makePool() {

		pool = fcmarea.initializeFCMMatrixPool(poolSize,
				fcmarea.getCsvNormalizedParser());
	}

	// suma fitness dla calej populacji
	public double getPoolFitness() {
		double fit = 0;
		for (FCMMatrix m : pool) {
			fit += m.getFitnes();
		}
		sumFit = fit;
		return fit;
	}
	
	

	// wylicze fitness dla populacji
	public void runFitnesCalculation() {
		for (FCMMatrix m : pool) {
			List<double[]> newFactorsVal = fcmarea.computateNewFactors(m); // nowe
																			// wart
																			// czynnikow
			m.setError(fcmarea.computateJError(newFactorsVal)); // wyliczenie
																// bledu
			m.setFitnes(fcmarea.computateFitness(m.getError())); // wyliczenie
																	// fitness
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

	public FCMMatrix selection(List<FCMMatrix> pool) {
		sumFit = this.getPoolFitness();

		double rand = GG.getNormalDistNumber(0, sumFit);
		int index = 0;
		double currFitness = 0;
		for (FCMMatrix m : pool) {
			if ((rand >= currFitness) && (rand <= currFitness + m.getFitnes())) {
				//!!!
				//pool.remove(m);
				//!!!
				return m;
			}

			currFitness += m.getFitnes();
			

			// if(rand - m.getFitnes())
		}

		return null;
	}
	
	public void runGA(int n){
		for(int i=0;i<n;i++){
			
			this.runFitnesCalculation();
			Collections.sort(this.getPool());
			
			this.makeNewPopulation();
		}
	}
	
	public void makeNewPopulation(){
		newPool.clear();
		tmpPool.clear();
		//wybieranie elity
		Collections.sort(this.getPool());
		List<FCMMatrix> mut = new ArrayList<>();
		
		int size = pool.size();
		
		//System.out.println("Old Pool Size = "+size);
		
		newPool.add(pool.get(pool.size()-1));
		newPool.add(pool.get(pool.size()-2));
		//pool.remove(pool.size()-1);
		//pool.remove(pool.size()-1);
		
		
		//seleckja
		while(tmpPool.size()<size){
			FCMMatrix m = selection(pool);
			if(m==null) continue;
			
			tmpPool.add(m);
			//mut.add(m);
		}
		
		//System.out.println("tmp Pool Size  = "+tmpPool.size() );
		/*int p = 0;
		for(FCMMatrix m : tmpPool){
			if(m==null) System.out.println("NULL "+p);
			p++;
		}*/
		
		
		//krzyzowanie
		
		int numbross = (int) (crossoverFraction*tmpPool.size());
		//System.out.println(numbross);
		int pos = 0;
		while(pos<numbross){
			
			newPool.add(crossover(tmpPool));
			pos++;
		}
		
		//System.out.println("New Pool Size bef mut = "+newPool.size() );
		//mutacja
		int numbmut = tmpPool.size() - newPool.size();
		
		pos = 0;
		while(pos<numbmut){
			Random r = new Random();
			int index = r.nextInt(tmpPool.size());
			FCMMatrix m = tmpPool.get(index);
			if(m==null) continue;
			if(mutation(m)==true){
				pos++;
				newPool.add(m);
				//tmpPool.remove(index);
			}
		}
		
		//System.out.println("New Pool Size = "+newPool.size() );
		
		//pool = newPool;
		pool.clear();
		for(FCMMatrix m : newPool){
			pool.add(m);
		}
		
		return;
		
		
	}

	// zwraca liste dwoch potomkow po krzyzowaniu m1 i m2
	public List<FCMMatrix> crossover2(FCMMatrix m1, FCMMatrix m2) {

		double tm1[] = FCMParser.matrixToVector(m1.getFcmMatrix());
		double tm2[] = FCMParser.matrixToVector(m2.getFcmMatrix());

		double c1[] = new double[tm1.length]; // wektor dziecka 1
		double c2[] = new double[tm2.length]; // wektor dziecka 2

		// punkt przeceicia
		Random r = new Random();
		int rand = r.nextInt((tm1.length + 1) - 1) + 1;
		System.out.println("Przeciecie " + rand);
		// dziecko 1 i dzeicko 2
		for (int i = 0; i < tm1.length; i++) {
			if (i < rand) {
				c1[i] = tm1[i];
				c2[i] = tm2[i];
			} else {
				c1[i] = tm2[i];
				c2[i] = tm1[i];
			}
		}

		// FCMParser.printVector(c1);
		// FCMParser.printVector(c2);

		List<FCMMatrix> list = new ArrayList<>();
		FCMMatrix fc1 = new FCMMatrix(m1.getFactorsNames());
		fc1.setFcmMatrix(FCMParser.vectorToMatrix(c1, m1.getNumberOfFactors(),
				m1.getNumberOfFactors()));
		// System.out.println(fc1.toString());

		FCMMatrix fc2 = new FCMMatrix(m2.getFactorsNames());
		fc2.setFcmMatrix(FCMParser.vectorToMatrix(c2, m2.getNumberOfFactors(),
				m2.getNumberOfFactors()));

		list.add(fc1);
		list.add(fc2);

		return list;
	}
	
	public FCMMatrix crossover(List<FCMMatrix> list){
		Random r = new Random();
		int index1 = r.nextInt(list.size());
		
		FCMMatrix m1 = list.get(index1);
		
		int index2 = r.nextInt(list.size());
		while(index1==index2){
			index2 = r.nextInt(list.size());
		}
		
		FCMMatrix m2 = list.get(index2);
		
		
		
		FCMMatrix m = crossover(m1, m2);
		
		
		return m;
	}
	
	

	public FCMMatrix crossover(FCMMatrix m1, FCMMatrix m2) {
//bez prawdopodobienstwa
		double tm1[] = FCMParser.matrixToVector(m1.getFcmMatrix());
		double tm2[] = FCMParser.matrixToVector(m2.getFcmMatrix());

		double c1[] = new double[tm1.length]; // wektor dziecka 1

		// punkt przeceicia
		Random r = new Random();
		int rand = r.nextInt((tm1.length + 1) - 1) + 1;
		//System.out.println("Przeciecie " + rand);
		// dziecko 1
		for (int i = 0; i < tm1.length; i++) {
			if (i < rand) {
				c1[i] = tm1[i];

			} else {
				c1[i] = tm2[i];

			}
		}

		FCMMatrix fc1 = new FCMMatrix(m1.getFactorsNames());
		fc1.setFcmMatrix(FCMParser.vectorToMatrix(c1, m1.getNumberOfFactors(),
				m1.getNumberOfFactors()));

		return fc1;
	}

	public boolean mutation(FCMMatrix m) {
		//prawd mutacje w srodku !
		Random r = new Random();
		int mut = r.nextInt(100); //3% szansy na zmutowanie
		if(mut>5) return false; 
			else{
				double vec[] = FCMParser.matrixToVector(m.getFcmMatrix());
				
				int numb = r.nextInt(3-1)+1; //mutacja od 1 do 3 genow
				for(int i=0;i<numb;i++){
					int ii = r.nextInt(vec.length-1);
					if(vec[ii]!=0){
						double mutate = GG.getNormalDistNumber(-0.2, 0.2);
						if(vec[ii]+mutate>1){
							vec[ii]=vec[ii]-mutate;
							m.setFcmMatrix(FCMParser.vectorToMatrix(vec, m.getNumberOfFactors(), m.getNumberOfFactors()));

							return true;
						} else 
							if(vec[ii]-mutate<-1){
								vec[ii]=vec[ii]+mutate;
								m.setFcmMatrix(FCMParser.vectorToMatrix(vec, m.getNumberOfFactors(), m.getNumberOfFactors()));
								return true;
							} else {
								boolean oper = r.nextBoolean();
								if(oper) vec[ii]=vec[i]+mutate; else  vec[ii]=vec[ii]-mutate;
								m.setFcmMatrix(FCMParser.vectorToMatrix(vec, m.getNumberOfFactors(), m.getNumberOfFactors()));

								return true;
							}
						
					} 
				}
				return true;
			} 
	}

}
