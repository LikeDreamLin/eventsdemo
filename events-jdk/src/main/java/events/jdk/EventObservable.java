package events.jdk;

import java.util.Observable;

import events.common.Event;

public class EventObservable extends Observable
{
    private Event event;
    public void setEvent(Event event)
    {
        this.event = event;
        setChanged();
        notifyObservers();
    }

    public Event getEvent()
    {
        return event;
    }
    
}
