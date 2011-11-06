package chord;

import java.lang.Byte;

public class ChordKey {
	
	String id;
	Integer key;
	
	public ChordKey(Integer key){
		this.key=key;
	}
	
	public ChordKey(String id){
		this.id=id;
		this.key = Hash.hash(id);
	}
	
	public Integer getKey(){
		return key;
	}
	
	public boolean isBetween(ChordNode m, ChordNode n){
		if (key>m.getChordKey().getKey() && key<=n.getChordKey().getKey() )
			return true;
		return false;
	}

	
}
