package chord;

public class FingerTable {
	
	public static final int maxFingers = 6;
	ChordNode[] fingerTable = new ChordNode[maxFingers];
	
	public FingerTable(ChordNode cNode){
		for(int i=0;i<maxFingers;i++) {
			fingerTable[i]=cNode;
		}
	}
	
	public ChordNode getFinger(int index){
		return fingerTable[index];
	}
	
}