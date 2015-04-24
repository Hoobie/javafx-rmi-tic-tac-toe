package pl.edu.agh.util;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.edu.agh.TicTacToe;

import java.rmi.RemoteException;

public class SceneUtil {

    public static void makeEndDialog(final Window window, final TicTacToe ticTacToe, final String nick, String message) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);

        VBox dialogVBox = new VBox(20);
        dialogVBox.setAlignment(Pos.CENTER);

        Text text = new Text(message);
        dialogVBox.getChildren().add(text);

        Button button = new Button("OK");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                dialog.close();
                ((Stage) window).close();
                try {
                    ticTacToe.quit(nick, true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        dialogVBox.getChildren().add(button);

        Scene dialogScene = new Scene(dialogVBox, 220, 80);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
