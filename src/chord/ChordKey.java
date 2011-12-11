package chord;

import java.lang.Byte;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import security.Hash;

public class ChordKey extends UnicastRemoteObject implements ChordKeyInterface{
	
	private static final long serialVersionUID = -184042299264191344L;
	String id;
	Integer key;
	
	public ChordKey(Integer key) throws RemoteException{
		this.key=key;
	}
	
	/**
	 * Create an key which corresponds at the hash of id
	 * @param id
	 * @throws RemoteException
	 */
	public ChordKey(String id) throws RemoteException{
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
