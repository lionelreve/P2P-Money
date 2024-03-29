/**
 * Class which represent a node in the chord
 * 
 * @author Sebastien FIFRE, Lionel REVEILLERE
 */

package chord;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyPair;

import security.MySignature;
import transaction.Wallet;

public class ChordNode extends UnicastRemoteObject implements ChordInterface{
	
	private static final long serialVersionUID = 1613424494460703307L;

	private String id = null;
	private ChordKey chordKey = null;
	private ChordInterface predecessor = null;
	private ChordInterface successor = null;
	private FingerTableInterface fingerTable = null;
	private int index = 0;
	private KeyPair keyPair = null;
	private Wallet wallet = null;

	/** id MAX */
	private static int MAXid = (int) Math.pow(2, FingerTable.MAXFINGERS - 1) - 1;
	/** id MIN */
	private static int MINid = 0;
	/** Boolean for the Stay Stable thread */
	private boolean alive = true;

	public ChordNode(String id) throws RemoteException{
		this.create();
		this.setNodeId(id);
		// Initialize key of our node with an hash of his id
		this.chordKey = new ChordKey(id);
		// Initialize his fingerTable with himself
		this.fingerTable = new FingerTable(this);
		// Every node starts with 10 coins.
		this.wallet = new Wallet(10.0);
		// Launch a thread which stabilize our node every 100ms
		this.checkStable();
		try {
			this.setKeyPair(MySignature.generateKeyPair(chordKey.getKey()));
		} catch (Exception e) {
			System.err.println("La generation de keyPair est mauvaise.");
			e.printStackTrace();
		}
	}

	/**
	 * Initialize node predecessor and successor with himself
	 * @throws RemoteException
	 */
	private void create() throws RemoteException{
		predecessor = this;
		successor = this;
	}

	/**
	 * To join a chord which contains cNode
	 */
	@Override
	public void join(ChordInterface cNode) throws RemoteException{
		this.predecessor = this;
		successor = cNode.find_successor(this.getChordKey().getKey());
	}

	/**
	 * Method which returns the successor of the key passed in parameters
	 */
	public ChordInterface find_successor(int key) throws RemoteException{
		if (isBetween(key, this.getChordKey().getKey() + 1, successor
				.getChordKey().getKey())){
			return successor;
		}
		else { // forward the query around the circle
			ChordInterface cNode = closest_preceding_node(key);
			return cNode.find_successor(key);
		}
	}

	/**
	 * Search in fingerTable the highest predecessor of cKey
	 */
	public ChordInterface closest_preceding_node(int cKey) throws RemoteException{
		for (int i = FingerTable.MAXFINGERS - 1; i > 0; i--) {
			int fingerKey = fingerTable.getFinger(i).getChordKey().getKey();
			if (fingerTable.getFinger(i).isAlive()
					&& isBetween(fingerKey, this.getChordKey().getKey()+1, cKey-1)){
				return fingerTable.getFinger(i);
			}
		}
		return successor;
	}

	/**
	 * Return true if id is between begin and end else return false
	 */
	public boolean isBetween(int id, int begin, int end) throws RemoteException{
		return (begin < end && begin <= id && id <= end)
				|| (begin > end && ((begin <= id && id <= MAXid) || (MINid <= id && id <= end)))
				|| ((begin == end) && (id == begin));
	}

	/**
	 * Called periodically by method checkStable. I ask the successor about my predecessor, verify
	 * if my immediate successor is consistent, and tells the successor about me
	 */
	public synchronized void stabilize() throws RemoteException{
		ChordInterface cNode = successor.getPredecessor();
		if (isBetween(cNode.getChordKey().getKey(),
				this.getChordKey().getKey() + 1, successor.getChordKey()
						.getKey() - 1)) {
			successor = cNode;
		}
		this.successor.notify(this);
	}

	/**
	 * cNode thinks it might be our predecessor.
	 */
	public synchronized void notify(ChordInterface cNode) throws RemoteException{
		if (predecessor == null
				|| // cNode.g(predecessor, n)
				isBetween(cNode.getChordKey().getKey(), predecessor
						.getChordKey().getKey() + 1, this.getChordKey()
						.getKey() - 1))
			predecessor = cNode;
	}

	/**
	 * Called periodically by method checkStable. Update fingerTable. Variable index stores the
	 * index of the finger to fix
	 */
	public synchronized void fix_fingers() throws RemoteException{		
		index++;
		if (index > FingerTable.MAXFINGERS - 1) {
			index = 1;
		}
		this.fingerTable.setFinger(index,
				find_successor((int) ((this.getChordKey().getKey() + Math.pow(2,(index - 1)))
						% (int) Math.pow(2, FingerTable.MAXFINGERS - 1))));
	}

	/**
	 * Run a thread for the stabilization of the node
	 */
	private void checkStable(){
		new Thread(new Runnable() {
			public void run() {
				while (alive) {
					// While node still exist
					try {
						stabilize();
						fix_fingers();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	

	/**
	 * Kill the node (clean)
	 */
	public synchronized void delete() throws RemoteException{
		alive = false;
		predecessor.setSuccessor(successor);
		successor.setPredecessor(predecessor);
	}	

	/**
	 * Method which displays informations about node like his successor, his predecessor,
	 * his fingerTable and his wallet
	 */
	public synchronized String toString(){
		try {
			String res = "<NODE: "
					+ this.getChordKey().getKey()
					+ ", MONEY:" +
					+ this.getWallet().getMoney()
					+ ", PRED: "
					+ (predecessor == null ? predecessor : predecessor
							.getChordKey().getKey())
					+ ", SUCC: "
					+ (successor == null ? successor : successor.getChordKey()
							.getKey()) + "> ";
			res += "\n\tFingers Table: ";
			if (fingerTable.getFinger(1) != null) {
				res += "[";
				for (int i = 1; i < FingerTable.MAXFINGERS - 1; i++) {
					res += fingerTable.getFinger(i).getChordKey().getKey() + ", ";
				}
				res += fingerTable.getFinger(FingerTable.MAXFINGERS - 1)
						.getChordKey().getKey()
						+ "]";
			} else {
				res += "null";
			}
			res += "\n\n";
			return res;
		} catch (RemoteException e) {
			e.printStackTrace();
			return "error";
		}
	}

	public String getNodeId() {
		return id;
	}

	public void setNodeId(String id) {
		this.id = id;
	}

	public ChordKeyInterface getChordKey() throws RemoteException{
		return chordKey;
	}

	public void setPredecessor(ChordInterface predecessor) throws RemoteException{
		this.predecessor = predecessor;
	}

	public ChordInterface getPredecessor() throws RemoteException{
		return predecessor;
	}

	public void setSuccessor(ChordInterface successor) throws RemoteException{
		this.successor = successor;
	}

	public ChordInterface getsuccessor() throws RemoteException{
		return successor;
	}

	public boolean isAlive() throws RemoteException{
		return this.alive;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Wallet getWallet() {
		return wallet;
	}

}
