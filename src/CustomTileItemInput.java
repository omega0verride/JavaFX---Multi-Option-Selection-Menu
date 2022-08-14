import javafx.application.Platform;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;


public class CustomTileItemInput extends CustomTileItem<StackPane> {

    private final Scene scene;
    private final CustomSelectableTileOptionsPane tilePane;
    private final AddButton btn;
    private final ComboBox<String> optionsComboBox;
    private final TextField otherTextField;
    private String comboBoxDefaultOptionString = "Select";
    private String comboBoxOtherOptionString = "Other...";

    public CustomTileItemInput(Scene scene, CustomSelectableTileOptionsPane tilePane) {
        super(StackPane::new);

        this.scene = scene;
        this.tilePane = tilePane;
        getRootPane().getStyleClass().clear();
        getRootPane().setId("CustomTileItemInput");

        btn = new AddButton(20);
        btn.setIconOnly(true);
        btn.setId("CustomTileItemInputButton");

        otherTextField = new TextField();
        otherTextField.setId("CustomTileItemInputTextField");

        optionsComboBox = new ComboBox<>();
        seValueFactoryAndSelectionListener();
        otherTextField.setId("CustomTileItemInputComboBox");

        getChildren().addAll(btn, optionsComboBox, otherTextField);
        setState(CustomTileItemInputState.Button);

        otherTextField.setMaxWidth(100);
        setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        setButtonListener();
        addLooseFocusHandler();

        addDropDownListener();
        addTextFieldDoneListener();

    }

    public static void delayedThread(long delayMs, Runnable toRun) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException ignored) {
            }
            Platform.runLater(toRun);
        });
        t.setDaemon(true);
        t.start();
    }

    private void addTextFieldDoneListener() {
        otherTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                tilePane.addCustomOption(otherTextField.getText());
                setState(CustomTileItemInputState.Button);
            }
        });
    }

    private void addDropDownListener() {
        optionsComboBox.addEventHandler(ComboBox.ON_SHOWING, event -> {
            int index = optionsComboBox.getItems().indexOf(comboBoxDefaultOptionString);
            optionsComboBox.getSelectionModel().clearSelection(); // clear section before removing option to avoid automatic selection
            if (index != -1)
                optionsComboBox.getItems().remove(index);
        });

        optionsComboBox.addEventHandler(ComboBox.ON_HIDING, event -> {
            int index = optionsComboBox.getItems().indexOf(comboBoxDefaultOptionString);
            if (index == -1)
                optionsComboBox.getItems().add(comboBoxDefaultOptionString);
            optionsComboBox.getSelectionModel().select(comboBoxDefaultOptionString);
        });
    }

    private void seValueFactoryAndSelectionListener() {
        optionsComboBox.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setOnMousePressed(e -> {
                if (!cell.isEmpty()) {
                    String option = cell.getText();
                    if (option.equals(comboBoxDefaultOptionString))
                        return;
                    if (option.equals(comboBoxOtherOptionString))
                        this.setState(CustomTileItemInputState.Other);
                    else {
                        tilePane.selectOption(option);
                        setState(CustomTileItemInputState.Button);
                    }
                }
            });
            return cell;
        });
    }

    private void setState(CustomTileItemInputState state) {
        if (state == CustomTileItemInputState.Button) {
            optionsComboBox.setVisible(false);
            otherTextField.setVisible(false);
            btn.setVisible(true);
            setMaxWidth(10);
        } else if (state == CustomTileItemInputState.ComboBox) {
            optionsComboBox.setVisible(true);
            otherTextField.setVisible(false);
            btn.setVisible(false);
            setMaxWidth(getPrefWidth());
        } else {
            optionsComboBox.setVisible(false);
            otherTextField.setVisible(true);
            otherTextField.clear();
            otherTextField.requestFocus();
            btn.setVisible(false);
            setMaxWidth(getPrefWidth());
        }
    }

    private boolean targetIsFocusOwner(EventTarget target) {
        for (Node n : otherTextField.getChildrenUnmodifiable())
            if (target.equals(n))
                return true;
        for (Node n : getChildren())
            if (target.equals(n))
                return true;
        return false;
    }

    private void addLooseFocusHandler() {
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // no need to check if the node has the focus, we change states
            if (!targetIsFocusOwner(event.getTarget())) {
                setState(CustomTileItemInputState.Button);
            }
        });
    }

    private void setButtonListener() {
        btn.setOnMouseClicked(e -> {
            setState(CustomTileItemInputState.ComboBox);
            // optionsComboBox.show();
            // delay sometime in order to wait for the show operation
            delayedThread(10, optionsComboBox::show);
        });
    }

    public String getComboBoxDefaultOptionString() {
        return comboBoxDefaultOptionString;
    }

    public void setComboBoxDefaultOptionString(String comboBoxDefaultOptionString) {
        this.comboBoxDefaultOptionString = comboBoxDefaultOptionString;
    }

    public String getComboBoxOtherOptionString() {
        return comboBoxOtherOptionString;
    }

    public void setComboBoxOtherOptionString(String comboBoxOtherOptionString) {
        this.comboBoxOtherOptionString = comboBoxOtherOptionString;
    }

    public ComboBox<String> getOptionsComboBox() {
        return optionsComboBox;
    }
}
