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
		this.key = Hash.hash(id) % (int)Math.pow(2, FingerTable.MAXFINGERS-1);
		if(key < 0){
			key = 128 + key;
		}
	}
	
	public Integer getKey(){
		return key;
	}

	
}
