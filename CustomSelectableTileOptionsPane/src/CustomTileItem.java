import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CustomTileItem<T extends javafx.scene.layout.Pane> extends StackPane {

    private T root;

    public CustomTileItem(Node node, java.util.function.Supplier<T> supplier) {
        super();

        root = supplier.get();
        this.getChildren().add(root);

        if (node!=null) {
            root.getChildren().add(node);
            node.setId("CustomTileItemNode");
        }
        root.getStyleClass().add("CustomTileItem");
    }
    public CustomTileItem(String text, java.util.function.Supplier<T> supplier) {
       this(new Label(text), supplier);
    }

    public CustomTileItem(java.util.function.Supplier<T> supplier) {
        this((Node) null, supplier);
    }

    public T getRootPane(){
        return root;
    }

    public void setRootPane(T root){
        this.root=root;
    }

}
