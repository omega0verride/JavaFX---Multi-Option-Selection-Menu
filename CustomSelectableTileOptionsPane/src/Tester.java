import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class Tester extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        HBox root = new HBox();
        final Scene scene = new Scene(root, 600, 400);

        JMetro jMetro = new JMetro(Style.DARK);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);



        CustomSelectableTileOptionsPane moduleSelection = new CustomSelectableTileOptionsPane(scene, "opt1", "opt2");
        moduleSelection.addOption("AUTH");
        moduleSelection.addOption("INFO");
        moduleSelection.addOption("TCP ");
        moduleSelection.addOption("HTTPS");
        moduleSelection.addOption("SSL ");
        moduleSelection.addOption("HTTP");
        moduleSelection.addOption("long text");
        moduleSelection.addOption("test");
        root.getChildren().add(moduleSelection);


        jMetro.setScene(scene);
        stage.setScene(scene);
        stage.setAlwaysOnTop(false);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}