import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;


public class CustomSelectableTileOptionsPane extends StackPane {

    private ContextMenu menu;
    private MenuItem clearAllMenuItem;

    private HashSet<String> allOptions;

    private HashMap<String, Node> selectedOptions = new HashMap<>();

    // these should be parallel to each other
    private ComboBox<String> optionsComboBox;
    private HashSet<String> availableOptions = new HashSet<>();

    private EventHandler<MouseEvent> hideMenuEventHandler;

    Scene scene;
    private String comboBoxDefaultOptionString = "Select";
    private String comboBoxOtherOptionString = "Other...";

    public static EventType<CustomSelectableTileOptionsPaneEvent> SELECTION_CHANGED = new EventType<>("SELECTION_CHANGED");

    private CustomTileItemInput addOptionsTile;

    private FlowPane flowPane;

    private boolean addBtnLast = true;

    private boolean scrollable;


    public CustomSelectableTileOptionsPane(Scene scene, String... allOptions) {
        this.scene = scene;

        flowPane = new FlowPane();
        flowPane.setVgap(3);
        flowPane.setHgap(3);

        disableScroll();

        getStylesheets().add(getClass().getResource("customSelectableTileOptionsPane.css").toExternalForm());

        getStyleClass().add("CustomSelectableTileOptionsPane");

        CustomTileItemInput addItemsTile = new CustomTileItemInput(scene, this);
        setAddOptionsTile(addItemsTile);
        setOptions((HashSet<String>) Arrays.stream(allOptions).collect(Collectors.toSet()));

        menu = new ContextMenu();
        clearAllMenuItem = new MenuItem("Clear");
        menu.getItems().add(clearAllMenuItem);

        addContextMenuListener();

    }

    public void addContextMenuListener() {
        // use a copy to avoid ConcurrentModificationException
        clearAllMenuItem.setOnAction(e -> {
            for (String s : selectedOptions.keySet().toArray(new String[0])) {
                deselectOption(s);
            }
        });

        setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown())
                menu.show(scene.getWindow(), event.getScreenX(), event.getScreenY());
        });
    }


    public void setAddOptionsTile(CustomTileItemInput node) {
        setAddOptionsTile(node, true);
    }

    private void setAddOptionsTile(CustomTileItemInput node, boolean ignoreUpdate) {
        this.addOptionsTile = node;
        optionsComboBox = addOptionsTile.getOptionsComboBox();
        int size = flowPane.getChildren().size();
        if (size == 0)
            flowPane.getChildren().add(node);
        else
            flowPane.getChildren().set(size - 1, addOptionsTile);
        if (allOptions != null && !ignoreUpdate)
            setOptions(allOptions);
    }

    public Node getAddOptionsTile() {
        return flowPane.getChildren().get(flowPane.getChildren().size() - 1);
    }

    public void emitChange() {
        Event event = new CustomSelectableTileOptionsPaneEvent(SELECTION_CHANGED);
        this.fireEvent(event);
    }

    private void setOptions(HashSet<String> allOptions) {
        this.availableOptions.clear();
        optionsComboBox.getItems().clear();
        this.selectedOptions.clear();
        this.flowPane.getChildren().clear();

        this.allOptions = allOptions;
        for (String val : allOptions.toArray(new String[0])) { // use a copy to avoid concurrency
            allOptions.remove(val); // replace it with the uppercase version
            allOptions.add(val.toUpperCase());
            availableOptions.add(val.toUpperCase());
            optionsComboBox.getItems().add(getNewSelectionIndex(), val.toUpperCase());
        }
        setComboBoxOtherOptionString(comboBoxOtherOptionString, true);
        setComboBoxDefaultOptionString(comboBoxDefaultOptionString, true);
        optionsComboBox.getSelectionModel().selectLast();
        setAddOptionsTile(addOptionsTile);
        emitChange();
    }

    public HashSet<String> getOptions() {
        return allOptions;
    }

    public boolean addOption(String option) {
        String key = option.toUpperCase();
        if (!allOptions.add(key))
            return false;
        if (!availableOptions.add(key))
            return false;
        optionsComboBox.getItems().add(getNewSelectionIndex(), key);
        emitChange();
        return true;
    }

    public boolean removeOption(String option) {
        String key = option.toUpperCase();
        if (!allOptions.remove(key))
            return false;

        Node toRemove = selectedOptions.remove(key);
        if (toRemove != null)
            flowPane.getChildren().remove(toRemove);

        if (!availableOptions.remove(key))
            return false;
        optionsComboBox.getItems().remove(key);
        emitChange();
        return true;
    }


    public boolean selectOption(String option) {
        String key = option.toUpperCase();
        if (!allOptions.contains(key))
            return false;
        if (!availableOptions.remove(key))
            return false;
        optionsComboBox.getItems().remove(key);
        if (!selectedOptions.containsKey(key)) {
            selectedOptions.put(key, new CustomRemovableTileItem(key, this));
            flowPane.getChildren().add(getNewSelectionIndex(), selectedOptions.get(key));
        }
        emitChange();
        return true;
    }

    public boolean deselectOption(String option) {
        String key = option.toUpperCase();
        if (!allOptions.contains(key))
            return removeCustomOption(option);
        if (!selectedOptions.containsKey(key))
            return false;
        if (!availableOptions.add(key))
            return false;
        optionsComboBox.getItems().add(getNewSelectionIndex(), key);
        flowPane.getChildren().remove(selectedOptions.get(key));
        selectedOptions.remove(key);
        emitChange();
        return true;
    }

    public boolean addCustomOption(String option) {
        String key = option.toUpperCase();
        if (availableOptions.contains(key))
            return selectOption(option);
        if (!selectedOptions.containsKey(key)) {
            selectedOptions.put(key, new CustomRemovableTileItem(key, this));
            flowPane.getChildren().add(getNewSelectionIndex(), selectedOptions.get(key));
            emitChange();
        }
        return true;
    }

    public boolean removeCustomOption(String option) {
        String key = option.toUpperCase();
        if (allOptions.contains(key))
            return deselectOption(key);
        if (!selectedOptions.containsKey(key))
            return false;
        flowPane.getChildren().remove(selectedOptions.get(key));
        selectedOptions.remove(key);
        emitChange();
        return true;
    }

    public ComboBox<String> getOptionsComboBox() {
        return optionsComboBox;
    }

    public void setComboBoxDefaultOptionString(String comboBoxDefaultOptionString) {
        setComboBoxDefaultOptionString(comboBoxDefaultOptionString, false);
    }

    public void setComboBoxDefaultOptionString(String comboBoxDefaultOptionString, boolean init) {
        this.comboBoxDefaultOptionString = comboBoxDefaultOptionString;
        int size = optionsComboBox.getItems().size();
        if (init)
            optionsComboBox.getItems().add(comboBoxDefaultOptionString);
        else
            optionsComboBox.getItems().set(size - 1, comboBoxDefaultOptionString);
        addOptionsTile.setComboBoxDefaultOptionString(comboBoxDefaultOptionString);
    }

    public void setComboBoxOtherOptionString(String comboBoxOtherOptionString) {
        setComboBoxOtherOptionString(comboBoxOtherOptionString, false);
    }

    public void setComboBoxOtherOptionString(String comboBoxOtherOptionString, boolean init) {
        this.comboBoxOtherOptionString = comboBoxOtherOptionString;
        int size = optionsComboBox.getItems().size();
        if (init)
            optionsComboBox.getItems().add(comboBoxOtherOptionString);
        else
            optionsComboBox.getItems().set(size - 2, comboBoxOtherOptionString);
        addOptionsTile.setComboBoxOtherOptionString(comboBoxOtherOptionString);
    }

    public String getComboBoxDefaultOptionString() {
        return comboBoxDefaultOptionString;
    }

    public String getComboBoxOtherOptionString() {
        return comboBoxOtherOptionString;
    }

    public java.util.Set<String> getSelectedOptions() {
        return selectedOptions.keySet();
    }

    public String[] getSelectedOptionsAsArray() {
        return selectedOptions.keySet().toArray(new String[0]);
    }

    private int getNewSelectionIndex() {
        if (addBtnLast)
            return 0;
        return 1;
    }

    public boolean isAddBtnLast() {
        return addBtnLast;
    }

    public void setAddBtnLast(boolean addBtnLast) {
        this.addBtnLast = addBtnLast;
    }

    public void disableScroll() {
        this.scrollable = false;
        this.getChildren().clear();
        this.getChildren().add(flowPane);
        setMaxHeight(getPrefHeight());
        setMinHeight(getPrefHeight());
        setMaxWidth(getPrefWidth());
        setMinWidth(getPrefWidth());
    }

    public void enableScroll(double height, double width) {
        this.scrollable = true;
        this.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        this.getChildren().add(scrollPane);
        scrollPane.setContent(flowPane);
        if (height > 0) {
            setMaxHeight(height);
            setMinHeight(height);
            scrollPane.setFitToHeight(false);
        } else {
            scrollPane.setFitToHeight(true);
        }
        if (width > 0) {
            setMaxWidth(width);
            setMinWidth(width);
            scrollPane.setFitToWidth(false);
        } else {
            scrollPane.setFitToWidth(true);
        }

    }

    public void enableHorizontalScrollOnly(double width) {
        this.enableScroll(-1, width);
    }

    public void enableVerticalScrollOnly(double height) {
        this.enableScroll(height, -1);
    }

    public boolean isScrollable() {
        return scrollable;
    }
}
