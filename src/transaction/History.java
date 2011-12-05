package transaction;

import java.util.ArrayList;

import chord.ChordKey;
import chord.ChordNode;

public class History {

	public ArrayList<Transaction> historyTransactions;
	
	/*
	 * On refait
	 */
	public boolean transactionIsAllowed(ChordKey cKey, double amont){
		int montant = 0;
		for (Transaction t : historyTransactions) {
			if(t.getPeer()==cKey){
				montant+=t.getValue();
			}
			else if(t.getFrom()==cKey){
				montant-=t.getValue();
			}
		}
		return montant>amont;
	}
	
	
}
