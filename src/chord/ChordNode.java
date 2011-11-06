package chord;

import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class ChordNode {

 	private String id = null; 
	private ChordKey chordKey = null;
	private ArrayList<ChordNode> myNodes = null;
	private ChordNode predecessor = null;
	private ChordNode succesor= null;
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

	public void setSuccesor(ChordNode succesor) {
		this.succesor = succesor;
	}

	public ChordNode getSuccesor() {
		return succesor;
	}
	
	public ChordNode(String id){
		this.setNodeId(id);
		this.chordKey = new ChordKey(id);
		this.fingerTable = new FingerTable(this);
		this.create();
	}
		
	private void create() {
		predecessor=null;
		succesor=this;
	}

	public void join(ChordNode cNode){
		this.predecessor=null;
		succesor=cNode.find_successor(this.getChordKey());
	}

	/*
	 *  ask node n to find the successor of id
	 */
	public ChordNode find_successor(ChordKey key) {
	  if (key.isBetween(this,succesor)) //TODO
		return succesor;
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

}
