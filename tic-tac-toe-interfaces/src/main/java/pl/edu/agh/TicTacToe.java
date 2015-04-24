package pl.edu.agh;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicTacToe extends Remote {
    PlayerSign join(String nick, boolean singlePlayer) throws RemoteException;

    void makeTurn(String nick, int row, int col) throws RemoteException;

    void quit(String nick, boolean endGame) throws RemoteException;
}
