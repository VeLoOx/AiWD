package pl.aiwd.calculating.transformation;

public class Tranformator {
	
	private ITransform function;
	
	public Tranformator(ITransform func){
		function = func;
	}
	
	public double transform(double val){
		return function.transform(val);
	}

	public ITransform getFunction() {
		return function;
	}

	public void setFunction(ITransform function) {
		this.function = function;
	}
	
	

}
