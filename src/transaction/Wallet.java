package transaction;

import java.io.Serializable;

import chord.ChordNode;

/*
 * A wallet to stores the money available for a Node.
 * Normaly it can be construct again thanks to the private key.
 */
public class Wallet implements Serializable {

	private double money=0;
	
	/*
	 * To send coins, we use the wallets sendCoins method.
	 *  It takes three arguments: the peer, the address to send coins to 
	 *  (here we use the 'from' address we chose earlier) 
	 * and how much money to send. 
	 */
	public TransactionObject sendCoins(ChordNode peer, ChordNode from, double value){
		if (this.getMoney()>= value){		
			// return new Transaction(peer, from, value);
			return null;
		}
		else 
			return null;
	}
	
	/*
	 * Create a wallet with a given amount of money.
	 * @param money : the amount of money.
	 */
	public Wallet(double money){
		if (money>=0)
			this.money=money;
		else{
			System.err.println("Money can't be initializated with a negative value !");
			System.out.println("Money has been initializated with a 0 value.");
			money=0;
		}
	}
	
	/*
	 * Return true if the wallet contains more than money given.
	 * @param money : the amount of money. 
	 */
	public boolean hasMoney(double money){
		return this.getMoney()>=money;
	}
	
	// Now send the coins back!
	//assert sendTx != null;  // We should never try to send more coins than we have!
	//System.out.println("Sent coins back! Transaction hash is " + sendTx.getHashAsString());

	public double getMoney() {
		return money;
	}
	
	public void setMoney(double money) {
		if (money>=0)
			this.money=money;
		else{
			System.err.println("Money can't be a negative value !");
			System.out.println("Money value hasn't change.");
		}
	}
	
	public void giveMoney(double money) {
		if (money>=0){
			this.money -= money;
		}
		else{
			System.err.println("We can't give a negative value !");
			System.out.println("Money value hasn't change.");
		}
	}
	
	public void receiveMoney(double money) {
		if (money>=0){
			this.money += money;
		}
		else{
			System.err.println("We can't receive a negative value !");
			System.out.println("Money value hasn't change.");
		}
	}

}