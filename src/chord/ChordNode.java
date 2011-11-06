package chord;

import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class ChordNode {

 	private String id = null; 
	private ChordKey chordKey = null;
	private ArrayList<ChordNode> myNodes = null;
	private ChordNode predecessor = null;
	private ChordNode successor= null;
	private FingerTable fingerTable= null;
	
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
	
	public ChordNode(String id){
		this.setNodeId(id);
		this.chordKey = new ChordKey(id);
		this.fingerTable = new FingerTable(this);
		this.create();
	}
		
	private void create() {
		predecessor=null;
		successor=this;
	}

	public void join(ChordNode cNode){
		this.predecessor=null;
		successor=cNode.find_successor(this.getChordKey());
	}

	/*
	 *  ask node n to find the successor of id
	 */
	public ChordNode find_successor(ChordKey key) {
	  if (key.isBetween(this,successor)) //TODO
		return successor;
	  else { // forward the query around the circle
	    ChordNode cNode = closest_preceding_node(key);
	    return cNode.find_successor(key);
	  }
	}
	
	public ChordNode closest_preceding_node(ChordKey cKey){
		for (int i = FingerTable.maxFingers-1; i >= 0; i--) {
			int fingerKey = fingerTable.getFinger(i).getChordKey().getKey();
			if(fingerKey > this.getChordKey().getKey() && fingerKey < cKey.getKey())
				return fingerTable.getFinger(i);
		}		
		return this;
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
	   for (int index=0;index<FingerTable.maxFingers ;index++) //TODO
		   index++;		   // this.fingerTable.setFinger(index,find_successor(this.getChordKey().getKey()+2^(index−1)));
	}
	
	public void check_predecessor(){
		if(this.getPredecessor().equals(null)){ //TODO CALL THE PREDECESOR
			predecessor=null;
		}
	}
	
}
