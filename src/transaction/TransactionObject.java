package transaction;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import security.Hash;
import security.MySignature;
import chord.ChordKey;
import chord.ChordNode;

public class TransactionObject {

	ChordKey peer=null;
	ChordKey from=null;
	double value=0;
	byte[] signature=null;
	PublicKey publicKey= null;
	
	public TransactionObject(ChordKey peer, ChordKey from, double value, byte[] signature, PublicKey publicKey){
		this.peer=peer;
		this.from=from;
		this.value=value;
		this.signature=signature;
		this.publicKey= publicKey;
	}
	
	public TransactionObject(ChordNode from, ChordKey peer, double value) throws Exception{
		this.from= (ChordKey) from.getChordKey();
		this.peer= peer;
		this.value=value;
		String hash = Hash.encrypt2Hex(peer.getKey().toString() + from.getChordKey().getKey().toString() + value);
		this.signature = MySignature.signData(hash.getBytes(), from.getKeyPair().getPrivate());
		this.publicKey= from.getKeyPair().getPublic();
	}

	/*
	 * We verify the signature of a transaction.
	 */
	public static boolean VerifySignature(TransactionObject t) throws Exception{
		String hash = Hash.encrypt2Hex(t.getPeer().getKey().toString() + t.getFrom().getKey().toString() + t.getValue());
		return MySignature.verifySig(t.getSignature(), t.getPublicKey(), t.getSignature());
	}
	
	/*
	 * In order to verify a transaction we must verify 
	 * 1) That its wallet contains enough credit.
	 * 2) that the origin of the transaction is really the one who says it is.
	 */
	public static boolean VerifyTransaction(TransactionObject transaction) throws Exception{
		if (History.transactionIsAllowed(transaction))
			return VerifySignature(transaction);
		else
			return false;
	}
	
	public ChordKey getPeer() {
		return peer;}

	public ChordKey getFrom() {
		return from;}

	public double getValue() {
		return value;}

	public byte[] getSignature() {
		return signature;}

	public PublicKey getPublicKey() {
		return publicKey;}

}
