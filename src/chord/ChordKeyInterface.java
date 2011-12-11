package chord;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChordKeyInterface extends Remote{
	
	public Integer getKey() throws RemoteException;

}
