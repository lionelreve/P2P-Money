package transaction;

import security.Hash;
import chord.ChordNode;

public class Transaction {

	ChordNode peer=null;
	ChordNode from=null;
	double value=0;
	String hash=null;
	String publicKey= null;
	
	public Transaction(ChordNode peer, ChordNode from, double value, String hash, String publicKey, String privateKey){
		this.peer=peer;
		this.from=from;
		this.value=value;
		//this.hash = new Hash().getNodeId() + from.getNodeId() + value;
		this.publicKey=publicKey;
	}
	
}
