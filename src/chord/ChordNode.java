package chord;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChordNode extends UnicastRemoteObject implements ChordInterface{
	
	private static final long serialVersionUID = 1613424494460703307L;

	private String id = null;
	private ChordKey chordKey = null;
	private ArrayList<ChordInterface> nodes = null;
	private ChordInterface predecessor = null;
	private ChordInterface successor = null;
	private FingerTableInterface fingerTable = null;
	private int index = 0;

	/** id MAX */
	private static int MAXid = (int) Math.pow(2, FingerTable.MAXFINGERS - 1) - 1;
	/** id MIN */
	private static int MINid = 0;
	/** Boolean for the Stay Stable thread */
	private boolean alive = true;

	public ChordNode(String id) throws RemoteException{
		this.create();
		this.setNodeId(id);
		this.chordKey = new ChordKey(id);
		this.fingerTable = new FingerTable(this);
		this.checkStable();
	}

	private void create() throws RemoteException{
		predecessor = this;
		successor = this;
	}

	@Override
	public void join(ChordInterface cNode) throws RemoteException{
		System.out.println("join");
		this.predecessor = this;
		successor = cNode.find_successor(this.getChordKey().getKey());
	}

	/*
	 * ask node n to find the successor of id
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

	public ChordInterface closest_preceding_node(int cKey) throws RemoteException{
		for (int i = FingerTable.MAXFINGERS - 1; i > 0; i--) {
			int fingerKey = fingerTable.getFinger(i).getChordKey().getKey();
			if (fingerTable.getFinger(i).isAlive()
					&& isBetween(fingerKey, this.getChordKey().getKey()+1, cKey-1)){
				return fingerTable.getFinger(i);
			}
		}
		return this;
	}

	public boolean isBetween(int id, int begin, int end) throws RemoteException{
		return (begin < end && begin <= id && id <= end)
				|| (begin > end && ((begin <= id && id <= MAXid) || (MINid <= id && id <= end)))
				|| ((begin == end) && (id == begin));
	}

	/*
	 * called periodically. n asks the successor about its predecessor, verifies
	 * if n's immediate successor is consistent, and tells the successor about n
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

	/*
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

	/*
	 * called periodically. refreshes finger table entries. next stores the
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

	public void check_predecessor() throws RemoteException{
		if (this.getPredecessor().equals(null)) { // TODO CALL THE PREDECESOR
			predecessor = null;
		}
	}

	/**
	 * Run a thread for the stabilization of the node
	 */
	private void checkStable(){
		new Thread(new Runnable() {
			public void run() {
				while (alive) {
					// System.err.println("\tStabilization in progress for Node "
					// + getId());
					try {
						stabilize();
						fix_fingers();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					
					try {
						Thread.sleep(1000);
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
		//TODO add the objects hold by n to the successor.
		/*for (int key : fingerTable.keySet()) {
			try {
				successor.addObject(key, table.get(key));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}*/
	}

	public void addNode(ChordInterface cNode) throws RemoteException{
		nodes.add(cNode);
	}
	
	public void sortNodes() throws RemoteException{
		ArrayList<ChordInterface> res = new ArrayList<ChordInterface>();
		ChordInterface nodeMin;
		int i,j;
		while(nodes.size() != 0){
			i = 0;
			j = 0;
			nodeMin = nodes.get(0);
			for(ChordInterface n : nodes){
				if(n.getChordKey().getKey() < nodeMin.getChordKey().getKey()){
					nodeMin = n;
					i = j;
				}
				j++;
			}
			res.add(nodeMin);
			nodes.remove(i);
		}
		nodes = res;
	}
	
	public String display() throws RemoteException{
		String res = "";
		res += "________________________________________________________\n\n";
		sortNodes();
		for (ChordInterface n : nodes) {
			res += n;
		}
		res += "________________________________________________________\n";
		return res;
	}
	

	public synchronized String toString(){
		try {
			String res = "<NODE: "
					+ this.getChordKey().getKey()
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
		// affichage du contenu de la table.
		// if (!table.isEmpty()) {
		// res += "\n\tData Content : ";
		// for (Map.Entry<Integer, Object> entry : table.entrySet()) {
		// res += "\n\t  [" + entry.getKey() + "] - ";
		// res += entry.getValue().toString();
		// }
		// }
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

	public ArrayList<ChordInterface> getNodes()throws RemoteException{
		return nodes;
	}
	
	public void setNodes(ArrayList<ChordInterface> nodes) throws RemoteException{
		this.nodes = nodes;
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

}
