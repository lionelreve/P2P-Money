package chord;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChordInterface extends Remote{
	
	public void join(ChordInterface cNode) throws  RemoteException;
	
	public ChordInterface find_successor(int key) throws RemoteException;
	
	

}
