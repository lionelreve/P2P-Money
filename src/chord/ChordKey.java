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

	
}
