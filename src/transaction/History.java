package transaction;

import java.util.ArrayList;

import chord.ChordKey;

/*
 * The history of all the transactions done in the Chord.
 */
public class History {

	public static ArrayList<TransactionObject> historyTransactions = new ArrayList<TransactionObject>();
	public static double initialMoney = 10;
	
	
	/*
	 * Verify is the transaction is allowed.
	 * For this it calculate what remains in the wallet
	 * after all the transactions are done.
	 * @param  transaction : the transaction to verify
	 */
	public static boolean transactionIsAllowed(TransactionObject transaction){
		int montant = 0;
		if (historyTransactions.size() > 0)
			for (TransactionObject t : historyTransactions) {
				if(t.getPeer()==transaction.getFrom()){
					montant+=t.getValue();
				}
				else if(t.getFrom()==transaction.getFrom()){
					montant-=t.getValue();
				}
			}
		return (montant+initialMoney)>=transaction.getValue();
	}
	
	/*
	 * Verify is the transaction is allowed.
	 * For this it calculate what remains in the wallet
	 * after all the transactions are done.
	 * @param  transaction : the transaction to verify
	 */
	public static boolean transactionIsAllowed(ChordKey cKeyOrigin, double amont){
		int montant = 0;
		for (TransactionObject t : historyTransactions) {
			if(t.getPeer()==cKeyOrigin){
				montant+=t.getValue();
			}
			else if(t.getFrom()==cKeyOrigin){
				montant-=t.getValue();
			}
		}
		return (montant+initialMoney)>=amont;
	}
	
	/*
	 * Add a transaction in the history.
	 * @param  t : the transaction to add in the history
	 */
	public static void addTransaction(TransactionObject t){
		historyTransactions.add(t);
	}

	public static ArrayList<TransactionObject> getHistoryTransactions() {
		return historyTransactions;
	}
	
	public static String toString2(){
		String result = "Transaction Object :\n";
		int i = 1;
		if (historyTransactions.size() > 0)
			for (TransactionObject t : historyTransactions) {
				result += "\t" + i++ + ")" + t.toString();
			}
		else
			result += "\tno transaction done yet\n";
		return result;
	}
	
}
