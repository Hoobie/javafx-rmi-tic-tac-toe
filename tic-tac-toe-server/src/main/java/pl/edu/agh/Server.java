package pl.edu.agh;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static void main(String[] args) {
        try {
            TicTacToe ticTacToe = (TicTacToe) UnicastRemoteObject.exportObject(new TicTacToeImpl(), 0);
            Naming.rebind("rmi://localhost:1099/tic-tac-toe", ticTacToe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
