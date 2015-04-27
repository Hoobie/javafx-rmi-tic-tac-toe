package pl.edu.agh;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Listener extends Remote {
    void onMyTurn() throws RemoteException;

    void onOpponentsTurnEnd(int row, int col) throws RemoteException;

    void onWin() throws RemoteException;

    void onLoss() throws RemoteException;

    void onDraw() throws RemoteException;
}
