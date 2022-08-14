import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomRemoveButton extends Button {
    String iconPath = "remove_icon_red.png";

    public CustomRemoveButton() {
        setText("Remove");
        setFocusTraversable(false);
        setGraphic(getImage());
    }

    public CustomRemoveButton(int size) {
        this();
        setIconSize(size);
    }

    private ImageView getImage() {
        return new ImageView(String.valueOf(CustomRemoveButton.class.getResource(iconPath)));
    }

    private ImageView getResizedImage(int size) {
        Image image = new Image(String.valueOf(CustomRemoveButton.class.getResource(iconPath)), size, size, true, true);
        return new ImageView(image);
    }

    public void setIconOnly(boolean value) {
        if (value)
            setText("");
    }

    public void setIconSize(int size) {
        setGraphic(getResizedImage(size));
    }
}
