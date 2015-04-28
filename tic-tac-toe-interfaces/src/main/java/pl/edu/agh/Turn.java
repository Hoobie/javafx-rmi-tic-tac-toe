package pl.edu.agh;

import java.io.Serializable;

public class Turn implements Serializable {
    public int row;
    public int col;

    public Turn(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
