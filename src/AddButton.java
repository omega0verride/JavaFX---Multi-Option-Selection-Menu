import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddButton extends Button {
    String iconPath="plus_icon.png";
    public AddButton() {
        setText("Add");
        setFocusTraversable(false);
        setGraphic(getImage());
    }

    public AddButton(int size){
        this();
        setIconSize(size);
    }

    private ImageView getImage() {
        return new ImageView(String.valueOf(AddButton.class.getResource(iconPath)));
    }

    private ImageView getResizedImage(int size) {
        Image image = new Image(String.valueOf(AddButton.class.getResource(iconPath)), size, size, true, true);
        return new ImageView(image);
    }

    public void setIconOnly(boolean value){
        if (value)
            setText("");
    }

    public void setIconSize(int size){
        setGraphic(getResizedImage(size));
    }
}
