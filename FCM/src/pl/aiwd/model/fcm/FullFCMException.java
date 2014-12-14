package pl.aiwd.model.fcm;

public class FullFCMException extends Exception {
	
	public FullFCMException(){
		super("Mapa jest pelna. Nie mozna dodac kolejnego czynnika\n");
	}

}
