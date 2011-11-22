package chord;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class ChordNode {

 	private String id = null; 
	private ChordKey chordKey = null;
	private ArrayList<ChordNode> myNodes = null;
	private ChordNode predecessor = null;
	private ChordNode successor= null;
	private FingerTable fingerTable= null;
	private int index = 0;
	
	/** id MAX */
	private static int MAXid = (int) Math.pow(2, FingerTable.MAXFINGERS - 1) - 1;
	/** id MIN */
	private static int MINid = 0;
	/** Boolean for the Stay Stable thread */
	private boolean alive = true;
	
	public ChordNode(String id){
		this.create();
		this.setNodeId(id);
		this.chordKey = new ChordKey(id);
		this.fingerTable = new FingerTable(this);
		this.checkStable();
	}
		
	private void create() {
		predecessor=this;
		successor=this;
	}

	public void join(ChordNode cNode){
		this.predecessor=null;
		successor=cNode.find_successor(this.getChordKey().getKey());
	}

	/*
	 *  ask node n to find the successor of id
	 */
	public ChordNode find_successor(int key) {
	  if (isBetween(key, this.getChordKey().getKey()+1,successor.getChordKey().getKey())) //TODO
		return successor;
	  else { // forward the query around the circle
	    ChordNode cNode = closest_preceding_node(key);
	    return cNode.find_successor(key);
	  }
	}
	
	public ChordNode closest_preceding_node(int cKey){
		for (int i = FingerTable.MAXFINGERS-1; i >= 0; i--) {
			int fingerKey = fingerTable.getFinger(i).getChordKey().getKey();
			System.out.println(isBetween(fingerKey,this.getChordKey().getKey(),cKey));
			if(fingerTable.getFinger(i).isAlive() && isBetween(fingerKey,this.getChordKey().getKey(),cKey))
				return fingerTable.getFinger(i);
		}		
		return this;
	}
	
	public boolean isBetween(int id, int begin, int end){
		return (begin < end && begin <= id && id <= end)
		|| (begin > end && ((begin <= id && id <= MAXid) || (MINid <= id && id <= end)))
		|| ((begin == end) && (id == begin));
	}

	
	
	/*
	 * called periodically. n asks the successor
	 * about its predecessor, verifies if n's immediate
	 * successor is consistent, and tells the successor about n 
	 */
	public void stabilize(){
		ChordNode cNode = successor.getPredecessor();
		if (cNode.getChordKey().getKey()>this.getChordKey().getKey() 
		 && cNode.getChordKey().getKey()<successor.getChordKey().getKey())
		{
			successor = cNode;
		}
		successor.notify(this);
	}

	/*
	 * cNode thinks it might be our predecessor.
	 */
	public void notify(ChordNode cNode) {
	   if (predecessor.equals(null) || // cNode.g(predecessor, n)
		  (cNode.getChordKey().getKey()>predecessor.getChordKey().getKey()
		 && cNode.getChordKey().getKey()<this.getChordKey().getKey()))	   
	     predecessor = cNode;
	}

	
	/*
	 * called periodically. refreshes finger table entries.
	 * next stores the index of the finger to fix
	 */
	public void fix_fingers(){
		index++;
		if(index > FingerTable.MAXFINGERS-1){
			index = 1;
		}
		this.fingerTable.setFinger(index,find_successor(this.getChordKey().getKey()+2^(index-1)));
	}
	
	public void check_predecessor(){
		if(this.getPredecessor().equals(null)){ //TODO CALL THE PREDECESOR
			predecessor=null;
		}
	}
	
	/**
	 * Run a thread for the stabilization of the node
	 */
	private void checkStable() {
		new Thread(new Runnable() {
			public void run() {
				while (alive) {
					// System.err.println("\tStabilization in progress for Node "
					// + getId());
					stabilize();
					fix_fingers();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public synchronized String toString() {
		String res = "<NODE: " + this.getChordKey().getKey() + ", PRED: "
				+ (predecessor == null ? predecessor : predecessor.getChordKey().getKey())
				+ ", SUCC: "
				+ (successor == null ? successor : successor.getChordKey().getKey()) + "> ";
		res += "\n\tFingers Table: ";
		if (fingerTable.getFinger(1) != null) {
			res += "[";
			for (int i = 1; i < FingerTable.MAXFINGERS - 1; i++) {
				res += fingerTable.getFinger(i).getChordKey().getKey() + ", ";
			}
			res += fingerTable.getFinger(FingerTable.MAXFINGERS - 1).getChordKey().getKey() + "]";
		} else {
			res += "null";
		}
		// affichage du contenu de la table.
//		if (!table.isEmpty()) {
//			res += "\n\tData Content : ";
//			for (Map.Entry<Integer, Object> entry : table.entrySet()) {
//				res += "\n\t  [" + entry.getKey() + "] - ";
//				res += entry.getValue().toString();
//			}
//		}
		res += "\n\n";
		return res;
	}
	
	public String getNodeId(){
		return id;
	}
	
	public void setNodeId(String id){
		this.id = id;
	}
	
	public ChordKey getChordKey(){
		return chordKey;
	}
	
	public ArrayList<ChordNode> getMyNodes(){
		return myNodes;
	}
	
	public void setPredecessor(ChordNode predecessor) {
		this.predecessor = predecessor;
	}

	public ChordNode getPredecessor() {
		return predecessor;
	}

	public void setsuccessor(ChordNode successor) {
		this.successor = successor;
	}

	public ChordNode getsuccessor() {
		return successor;
	}
	
	public boolean isAlive(){
		return this.alive;
	}
	
}
