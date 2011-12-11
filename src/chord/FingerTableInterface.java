package chord;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FingerTableInterface extends Remote{
	
	public ChordInterface getFinger(int index) throws RemoteException;
	
	public void setFinger(int index, ChordInterface cNode) throws RemoteException;
}
