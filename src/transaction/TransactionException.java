package transaction;

public class TransactionException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	*/  
	public TransactionException() {}  
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	* @param message Le message détaillant exception 
	*/  
	public TransactionException(String message) { 
		super(message); 
		System.err.println("Transaction not allowed");
	}  
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public TransactionException(Throwable cause) {  
		super(cause); 
		System.err.println("Transaction not allowed");
	}  
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	* @param message Le message détaillant exception 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public TransactionException(String message, Throwable cause) {  
		super(message, cause); 
		System.err.println("Transaction not allowed");
	} 

}
