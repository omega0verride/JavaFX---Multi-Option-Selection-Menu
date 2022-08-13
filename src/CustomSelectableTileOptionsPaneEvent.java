import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class CustomSelectableTileOptionsPaneEvent extends Event {

    public CustomSelectableTileOptionsPaneEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public CustomSelectableTileOptionsPaneEvent(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }
}
