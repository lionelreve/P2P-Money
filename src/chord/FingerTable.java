package chord;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FingerTable extends UnicastRemoteObject implements FingerTableInterface{
	
	private static final long serialVersionUID = 1L;
	public static final int MAXFINGERS = 8;
	ChordInterface[] fingerTable = new ChordInterface[MAXFINGERS];
	
	/**
	 * Initialize fingerTable with cNode
	 * @param cNode
	 * @throws RemoteException
	 */
	public FingerTable(ChordInterface cNode) throws RemoteException{
		for(int i=1;i<MAXFINGERS;i++) {
			fingerTable[i]=cNode;
		}
	}
	
	public ChordInterface getFinger(int index) throws RemoteException{
		return fingerTable[index];
	}

	public void setFinger(int index, ChordInterface cNode) throws RemoteException{
		fingerTable[index]= cNode;
	}
}