package transaction;

import java.util.ArrayList;

import chord.ChordKey;
import chord.ChordNode;

public class History {

	public static ArrayList<TransactionObject> historyTransactions = new ArrayList<TransactionObject>();
	
	
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
		return (montant+10)>=transaction.getValue();
	}
	
	/*
	 * On refait
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
		return (montant+10)>=amont;
	}
	
	public void addTransaction(TransactionObject t){
		this.historyTransactions.add(t);
	}

	public ArrayList<TransactionObject> getHistoryTransactions() {
		return historyTransactions;
	}

	public void setHistoryTransactions(ArrayList<TransactionObject> historyTransactions) {
		this.historyTransactions = historyTransactions;
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
