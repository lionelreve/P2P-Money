package transaction;

import java.util.ArrayList;

import chord.ChordKey;
import chord.ChordNode;

public class History {

	public static ArrayList<Transaction> historyTransactions;
	
	
	public static boolean transactionIsAllowed(Transaction transaction){
		int montant = 0;
		for (Transaction t : historyTransactions) {
			if(t.getPeer()==transaction.getFrom()){
				montant+=t.getValue();
			}
			else if(t.getFrom()==transaction.getFrom()){
				montant-=t.getValue();
			}
		}
		return montant>transaction.getValue();
	}
	
	/*
	 * On refait
	 */
	public static boolean transactionIsAllowed(ChordKey cKeyOrigin, double amont){
		int montant = 0;
		for (Transaction t : historyTransactions) {
			if(t.getPeer()==cKeyOrigin){
				montant+=t.getValue();
			}
			else if(t.getFrom()==cKeyOrigin){
				montant-=t.getValue();
			}
		}
		return montant>amont;
	}
	
	public void addTransaction(Transaction t){
		this.historyTransactions.add(t);
	}

	public ArrayList<Transaction> getHistoryTransactions() {
		return historyTransactions;
	}

	public void setHistoryTransactions(ArrayList<Transaction> historyTransactions) {
		this.historyTransactions = historyTransactions;
	}
	
	
}
