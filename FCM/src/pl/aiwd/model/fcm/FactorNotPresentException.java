package pl.aiwd.model.fcm;

public class FactorNotPresentException extends Exception {
	
	public FactorNotPresentException(String name){
		super("Brak czynnika : !"+name+"! w mapie");
	}

}
