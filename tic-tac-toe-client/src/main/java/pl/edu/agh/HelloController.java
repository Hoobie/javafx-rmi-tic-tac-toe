package pl.edu.agh;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @FXML
    private Label messageLabel;

    public void sayHello() {
        log.info("[EVENT] Button clicked.");
        messageLabel.setText("Hello World!");
    }

}
