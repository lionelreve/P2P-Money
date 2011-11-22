package chord;

public class FingerTable {
	
	public static final int MAXFINGERS = 6;
	ChordNode[] fingerTable = new ChordNode[MAXFINGERS];
	
	public FingerTable(ChordNode cNode){
		for(int i=0;i<MAXFINGERS;i++) {
			fingerTable[i]=cNode;
		}
	}
	
	public ChordNode getFinger(int index){
		return fingerTable[index];
	}

	public void setFinger(int index, ChordNode cNode){
		fingerTable[index]= cNode;
	}
}