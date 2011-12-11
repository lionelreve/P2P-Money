package chord;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import transaction.Transaction;

public interface ChordInterface extends Remote{
	
	public void join(ChordInterface cNode) throws  RemoteException;
	
	public ChordInterface find_successor(int key) throws RemoteException;
	
	public ChordInterface closest_preceding_node(int cKey) throws RemoteException;
	
	public boolean isBetween(int id, int begin, int end) throws RemoteException;
	
	public void stabilize() throws RemoteException;
	
	public void notify(ChordInterface cNode) throws RemoteException;
	
	public void fix_fingers() throws RemoteException;
	
	public ChordKeyInterface getChordKey() throws RemoteException;
	
	public ChordInterface getPredecessor() throws RemoteException;
	
	public void setPredecessor(ChordInterface predecessor) throws RemoteException;
	
	public void setSuccessor(ChordInterface successor) throws RemoteException;
	
	public boolean isAlive() throws RemoteException;
	
	
}
