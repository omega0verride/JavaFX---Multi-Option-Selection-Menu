import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.util.Arrays;

public class Tester extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        HBox root = new HBox();
        final Scene scene = new Scene(root, 600, 400);

        JMetro jMetro = new JMetro(Style.DARK);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);


        CustomSelectableTileOptionsPane moduleSelection = new CustomSelectableTileOptionsPane(scene, "opt1", "opt2");
//        moduleSelection.setAddBtnLast(true);
//        moduleSelection.enableVerticalScrollOnly(130);
        moduleSelection.addOption("AUTH");
        moduleSelection.addOption("INFO");
        moduleSelection.addOption("TCP ");
        moduleSelection.addOption("HTTPS");
        moduleSelection.addOption("SSL ");
        moduleSelection.addOption("HTTP");
        moduleSelection.addOption("long text");
        moduleSelection.addOption("test");
        root.getChildren().add(moduleSelection);

        moduleSelection.addEventHandler(CustomSelectableTileOptionsPane.SELECTION_CHANGED, new EventHandler<CustomSelectableTileOptionsPaneEvent>() {
                    @Override
                    public void handle(CustomSelectableTileOptionsPaneEvent customSelectableTileOptionsPaneEvent) {
                        System.out.println(Arrays.toString(moduleSelection.getSelectedOptionsAsArray()));
                    }
                }
        );
        jMetro.setScene(scene);
        stage.setScene(scene);
        stage.setAlwaysOnTop(false);
        stage.show();
    }
}