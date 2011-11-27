package chord;

public class FingerTable {
	
	public static final int MAXFINGERS = 8;
	ChordNode[] fingerTable = new ChordNode[MAXFINGERS];
	
	public FingerTable(ChordNode cNode){
		for(int i=1;i<MAXFINGERS;i++) {
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