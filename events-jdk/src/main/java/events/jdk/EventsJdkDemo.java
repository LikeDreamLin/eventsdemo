package events.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.common.Event;
import events.common.Utils;

public class EventsJdkDemo
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsJdkDemo.class);
    private final EventObservable observable;
    
    public static void main(String[] args)
    {
        EventsJdkDemo demo = new EventsJdkDemo();
        demo.sendEvent(Event.FINISH_WORKING);
        Utils.sleep();
    }

    public EventsJdkDemo()
    {
        observable = new EventObservable();
        observable.addObserver(new LoggerObserver(observable));
    }

    public void sendEvent(Event event)
    {
        LOGGER.info("Sending event {}", event);
        observable.setEvent(event);
    }

}
