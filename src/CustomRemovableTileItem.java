import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class CustomRemovableTileItem extends CustomTileItem<HBox> {
    private HBox root;
    private String key;


    public CustomRemovableTileItem(String key, Node node, CustomSelectableTileOptionsPane tilePane) {
        super(node, HBox::new);
        root = getRootPane();

        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);
        root.getChildren().add(spacer);

        CustomRemoveButton button = new CustomRemoveButton(15);
        button.setIconOnly(true);
        button.setId("CustomRemovableClearButton");
        button.setOnMouseClicked(e -> tilePane.deselectOption(key));
        root.getChildren().add(button);

        root.setId("CustomRemovableTileItem");
        root.getStyleClass().add("CustomTileItem");
    }

    public CustomRemovableTileItem(String key, CustomSelectableTileOptionsPane tilePane) {
        this(key, new Label(key), tilePane);
    }

    public HBox getRoot() {
        return root;
    }

    public void setRoot(HBox root) {
        this.root = root;
    }

    public String getKey() {
        return key;
    }

}
